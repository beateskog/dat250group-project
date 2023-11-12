import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
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

  isConfirmationDialogOpen!: boolean;
  pollIdToDelete: number | null = null;
  confirmationMessage!: string;


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
  accounts!: Account[]

  account: { id: number, username: string, role: string, numberOfPolls: number, polls: [] } = { id: 0, username: '', role: '', numberOfPolls: 0, polls: []};

  constructor(private router: Router, private pollService: PollService, private authService: AuthService, private accountService: AccountService, private http: HttpClient, private changeDetectorRef: ChangeDetectorRef) {
    if (this.authService.getToken()) {
      this.isAuthenticated = true;
    }
  }

  ngOnInit() {
    const authToken = this.authService.getToken();
    // localStorage.setItem('role', )


    if (!authToken) {
      this.getPublicPolls();
      this.isAuthenticated = false;
    } 
    
    else {
      const loggedInUsername = localStorage.getItem('loggedInUsername');
      // this.username = this.accountService.getUsername();
      // localStorage.setItem('loggedInUsername', this.username);

      this.getPolls();
      if (loggedInUsername !== null) {

        this.isAuthenticated = true;
        console.log("polls: ", this.polls)
        this.username = loggedInUsername
  
        this.accountService.getAccountByUsername(this.username).subscribe(
          (account) => {
            localStorage.setItem('loggedInRole', account.role)
            const loggedInRole = localStorage.getItem('loggedInRole');
            if (loggedInRole !== null) {
              this.role = loggedInRole
            }
            console.log("Role: ", this.role);
            if (this.role == "ADMIN") {
              this.isAdmin = true;
              console.log("You are now logged in as administrator.");
  
              this.accountService.getAllAccounts().subscribe(
                (account: Account[]) => {
                  this.accounts = account;
                  console.log("The accounts are: ", this.accounts)
                },
                (error) => {
                  console.error('Error fetching accounts:', error);
                }
              );
            }
          },
          (error: any) => {
            this.error = "Something went wrong when trying to fetch the account";
            console.error("Login failed:", error);
          }
        );

      }
    }
  }

  private getPolls() {
    this.pollService.getPolls().subscribe(
      (polls: any) => {
        this.polls = polls;
        console.log("Fetched polls: ", this.polls)
      },
      (error) => {
        console.error('Error fetching polls:', error);
      }
    );
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


  deletePoll(id: number): void {
  
    // Make sure to subscribe to the HTTP request
    this.pollService.deletePoll(id).subscribe(
      (response) => {
        // Handle successful deletion here
        console.log("the response is: ", response)
        console.log(`Poll with ID ${id} deleted successfully`);
        this.polls = this.polls.filter((poll) => poll.id !== id);
        // const index = this.polls.findIndex((poll) => poll.id === id);
        // if (index !== -1) {
        //   this.polls.splice(index, 1);
        //   console.log(this.polls)
        // }
        // You might want to refresh the list of polls or update the view as needed
      },
      (error) => {
        // Handle error here
        console.error(`Error deleting poll with ID ${id}:`, error);
      }
    );
  }

  onNoClick(): void {
    this.isConfirmationDialogOpen = false;
    this.pollIdToDelete = null;
  }

  onYesClick(): void {
    console.log("Pollid to delete: ", this.pollIdToDelete)
    if (this.pollIdToDelete !== null) {
      this.isConfirmationDialogOpen = false;

      // Perform the poll deletion and remove it from the list using this.pollIdToDelete
      this.pollService.deletePoll(this.pollIdToDelete).subscribe(
        () => {
          // Poll deletion successful, remove it from the list
          console.log(`Poll ${this.pollIdToDelete} was deleted successfully!`)
        },
        (error) => {
          console.error('Error deleting poll:', error);
        }
      );

      this.confirmationMessage = '';
      this.pollIdToDelete = null;
    }
  }

  deleteAccount(accountId: number) {
    if (confirm("Are you sure you want to delete this account? This action cannot be undone.")) {
      // Send a DELETE request to delete the account
      this.accountService.deleteAccountById(accountId).subscribe(
        () => {
          // Account deleted successfully, remove it from the list
          this.accounts = this.accounts.filter(account => account.id !== accountId);
          this.changeDetectorRef.detectChanges();
        },
        (error) => {
          console.error('Error deleting account:', error);
        }
      );
    }
  }
  
  trackAccountBy(index: number, account: Account): number {
    return account.id
  } 

  openConfirmationDialog(pollId: number): void {
    this.pollIdToDelete = pollId; // Ensure pollIdToDelete is assigned correctly
    console.log('Poll ID to delete:', this.pollIdToDelete); // Add a console log for debugging
    this.isConfirmationDialogOpen = true;
    this.confirmationMessage = 'Are you sure you want to delete this poll?';
  }

  logout() {
    this.authService.removeToken();
    this.router.navigate(['/login'])
  }

}