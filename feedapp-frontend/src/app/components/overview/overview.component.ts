import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { PollService } from 'src/app/services/poll.service';
import { LoginComponent } from '../login/login.component';
import { Account, AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.css']
})
export class OverviewComponent {

adminFunctionality() {
}
navigateToLogin() {
  this.router.navigate(['/login']);
}

  polls: any[] = []; 
  id!: number;
  pollUrl!: string;
  userId!: number;
  username!: string;
  role!: string;
  publicPollsOnly: boolean = false
  errorMessage: string | null = null;
  isAuthenticated: boolean = false;
  error!: string;
  isAdmin: boolean = false;

  account: { id: number, username: string, role: string, numberOfPolls: number, polls: [] } = { id: 0, username: '', role: '', numberOfPolls: 0, polls: []};

  constructor(private router: Router, private pollService: PollService, private authService: AuthService, private accountService: AccountService, private http: HttpClient) {
    if (this.authService.getToken()) {
      this.isAuthenticated = true;

    }
  }

  ngOnInit() {
    const authToken = this.authService.getToken();
      if (!authToken) {
        this.getPublicPolls();
        this.isAuthenticated = false;
      } else {
        this.username = this.accountService.getUsername();
        this.getPolls();
        this.isAuthenticated = true


        this.accountService.getAccountByUsername(this.username).subscribe(
          (account) => {
            this.role = account.role
            console.log("Role: ", this.role)
            if (this.role == "ADMIN") {
              this.isAdmin = true;
              console.log("You are now logged in as administrator.");
            }
          },
          (error: any) => {
            this.error = "Something went wrong when trying to fetch the account";
            console.error("Login failed:", error);
          }
        );
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
    if (!this.isAuthenticated) {
      this.pollService.searchPublicPollsById(this.id).subscribe(
          (poll: any) => {
            console.log("Poll: ", poll)
            if (this.isPollActive(poll)) {
              // A poll with the given ID exists
              this.router.navigate([`/vote`, id]);
            } 
            else if (!this.isPollActive(poll)) {
              this.navigateToResults(id);
            }
            else {
              // No poll found for the provided ID
              this.errorMessage = `There is no poll with ID ${id}`;
            }
          },
          (error:any) => {
            if (error.status === 404) {
              // Poll not found (404 error)
              this.errorMessage = `There is no poll with ID ${id}`;;
            } else {
              this.errorMessage = 'The poll ID must be a number. Please try again.'
              console.error('Error searching for the poll:', error);
            }
          }
      )
      console.log("Not authenticated")
 
   } else {
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
          this.errorMessage = `There is no poll with ID ${id}`;
        } else {
          this.errorMessage = 'The poll ID must be a number. Please try again.';
          console.error('Error searching for the poll:', error);
        }
      }
    );
    }
  }

  searchPollsByURL(pollUrl: string) {

    const urlPattern = /^http:\/\/localhost:8080\/poll\/\d+$/;
    if (!urlPattern.test(pollUrl)) {
      // The provided URL is not valid, display an error message
      this.errorMessage = 'Please enter a valid URL';
      return; // Exit the method
    }
      // If the URL is valid, extract the poll ID
    const urlParts = pollUrl.split('/');
    const pollId = Number(urlParts[urlParts.length - 1]); // You can use Number() or parseInt() as needed
    console.log(pollId)

    this.searchPollsById(pollId);
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