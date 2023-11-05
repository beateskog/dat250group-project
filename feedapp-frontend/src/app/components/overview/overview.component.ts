import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
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
  errorMessage: string | null = null;
  constructor(private router: Router, private pollService: PollService) {}

  ngOnInit() {
    this.pollService.getPolls().subscribe({
      next: (polls:any) => {this.polls = polls;}
      ,
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
        if (poll) {
          // A poll with the given ID exists
          this.router.navigate([`/vote`, id]);
        } else {
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

}