import { Component, OnInit } from '@angular/core';
import { PollService } from 'src/app/services/poll.service';
import { DateTime } from 'luxon';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';


@Component({
  selector: 'app-my-polls',
  templateUrl: './myPolls.component.html',
  styleUrls: ['./myPolls.component.css']
})


export class MyPollsComponent implements OnInit {

  userPolls: any[] = [];
  publicPolls: any[] = [];
  isConfirmationDialogOpen = false;
  confirmationMessage = '';
  noPollsMessage = ''
  pollIdToDelete: number | null = null;
  pollToUpdate: any;
  updatedPoll: any = null;
  isAuthenticated!: boolean;
  isUpdateFormVisible: { [key: number]: boolean } = {};
  public noPolls: boolean = false;
  errorMessage = '';
  updateSuccess: boolean = false;

  constructor(private pollService: PollService, private router: Router, private authService: AuthService) {
    if (this.authService.getToken()) {
      this.isAuthenticated = true;
    }
  }

  ngOnInit() {
    this.pollService.getUserPolls().subscribe((polls: any) => {
      if (polls.length === 0) {
        // Handle the case when the user has no polls
        this.noPolls = true;
        this.noPollsMessage = "It looks like you haven't made any polls yet. Create your first poll!";
      } else {
        this.userPolls = polls;
        this.userPolls.forEach(poll => this.isUpdateFormVisible[poll.id] = false);
      }
    });
  }

  updatePoll(updatedPoll: any) {
    if (updatedPoll) {
      // Convert the startTime to a DateTime object
      const startTime = DateTime.fromISO(updatedPoll.startTime);
      const currentTime = DateTime.local(); // Get the current time
        // Make the PUT request to update the poll
      this.pollService.updatePoll(updatedPoll).subscribe(
        (response) => {
          // Handle successful update, e.g., display a success message
          console.log('Poll updated successfully');
          // Clear the form
          this.isUpdateFormVisible[updatedPoll.id] = false;
          this.updateSuccess = true;
        },
        (error) => {
          // Handle update error, e.g., display an error message
          console.error('Error updating poll:', error);
        }
      );
    }
  }


  deletePoll(pollId: number): void {
    // Implement logic to delete the poll by its ID
    this.pollService.deletePoll(pollId).subscribe(
      () => {
        // Handle successful deletion, e.g., remove the poll from the userPolls array
        this.userPolls = this.userPolls.filter((poll) => poll.id !== pollId);
      },
      (error) => {
        // Handle deletion error, e.g., display an error message
        console.error('Error deleting poll:', error);
      }
    );
  }

  openConfirmationDialog(pollId: number): void {
    this.pollIdToDelete = pollId; // Ensure pollIdToDelete is assigned correctly
    console.log('Poll ID to delete:', this.pollIdToDelete); // Add a console log for debugging
    this.isConfirmationDialogOpen = true;
    this.confirmationMessage = 'Are you sure you want to delete this poll?';
  }

  onNoClick(): void {
    this.isConfirmationDialogOpen = false;
    this.pollIdToDelete = null;
  }

  onYesClick(): void {
    console.log("Pollid to delete: ", this.pollIdToDelete)
    if (this.pollIdToDelete !== null) {
      this.isConfirmationDialogOpen = false;

      this.pollService.deletePoll(this.pollIdToDelete).subscribe(
        () => {
          // Poll deletion successful, remove it from the list
          this.userPolls = this.userPolls.filter(poll => poll.id !== this.pollIdToDelete);
        },
        (error) => {
          console.error('Error deleting poll:', error);
        }
      );
      const deletedPollIndex = this.userPolls.findIndex(poll => poll.id === this.pollIdToDelete);
      if (deletedPollIndex !== -1) {
        this.userPolls.splice(deletedPollIndex, 1); // Remove the poll from the array
      }
      this.confirmationMessage = '';
      this.pollIdToDelete = null;
    }
  }

  navigateToOverview() {
    this.router.navigate(['/overview']);
  }

  navigateToLogin() {
    this.router.navigate(['/login'])
  }

  toggleUpdateFormVisibility(pollId: number) {
    this.isUpdateFormVisible[pollId] = !this.isUpdateFormVisible[pollId];
  }
}