import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { PollService } from 'src/app/services/poll.service';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.css']
})
export class OverviewComponent {
navigateToLogin() {
  this.router.navigate(['/login']);
}

  polls: any[] = []; 
  id!: number;
  userId!: number;
  publicPollsOnly: boolean = false
  errorMessage: string | null = null;
  constructor(private router: Router, private pollService: PollService, private authService: AuthService, private http: HttpClient) {}


  ngOnInit() {
    const authToken = this.authService.getToken();
      if (!authToken) {
        this.getPublicPolls();
      } else {
        this.getPolls();
      };
  }

  private getPolls() {
    this.pollService.getPolls().subscribe({
      next: (polls: any) => {
        this.polls = polls;
      },
      error: () => {}
    });
  }

  private getPublicPolls() {
    this.pollService.getPublicPolls().subscribe({
      next: (publicPolls: any) => {
        this.polls = publicPolls;
      },
      error: () => {}
    });
  }

  isPollActive(poll: any): boolean {
    const currentTime = new Date(); // Get the current time
    const endTime = new Date(poll.endTime); // Parse poll's end time
    return endTime > currentTime; // Return true if the poll is open, false if closed
  }

  searchPollsById(id: number) {
    this.id = id;

    this.pollService.searchPollsById(this.id).subscribe(
      (poll: any) => {
        if (this.isPollActive(poll)) {
          // A poll with the given ID exists
          this.router.navigate([`/vote`, id]);
        } 
        else if (!this.isPollActive(poll)) {
          this.navigateToResults(id)
        }
        else {
          // No poll found for the provided ID
          this.errorMessage = `There is no poll with ID ${id}`;
        }
      },
      (error) => {
        if (error.status === 404) {
          // Poll not found (404 error)
          this.errorMessage = `There is no poll with ID ${id}`;;
        } else {
          this.errorMessage = 'The poll ID must be a number. Please try again.'
          console.error('Error searching for the poll:', error);
        }
      }
    );
    }

  navigateToVote(question: string) {
    this.router.navigate(['/vote', question]);
  }

  navigateToResults(id: number) {
    this.pollService.getEndedPollResults
    this.router.navigate(['/results', id]);
  }

  logout() {
    this.authService.removeToken();
    this.router.navigate(['/login'])
  }

}