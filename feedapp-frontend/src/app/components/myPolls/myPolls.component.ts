import { Component, OnInit } from '@angular/core';
import { PollService } from 'src/app/services/poll.service';
import { PollListComponent } from '../poll-list/poll-list.component';
import { DateTime } from 'luxon';
import { PollPrivacy } from '../pollPrivacy.enum';
import { Router } from '@angular/router';
import { HttpHeaders } from '@angular/common/http';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-my-polls',
  templateUrl: './myPolls.component.html',
  styleUrls: ['./myPolls.component.css']
})


export class MyPollsComponent implements OnInit {

  userPolls: any[] = [];
  isConfirmationDialogOpen = false;
  confirmationMessage = '';
  noPollsMessage = ''
  pollIdToDelete: number | null = null;
  pollToUpdate: any;
  updatedPoll: any = null;
  // isUpdateFormVisible: boolean = true;
  isUpdateFormVisible: { [key: number]: boolean } = {};
  public noPolls: boolean = false;
  errorMessage = '';

  constructor(private pollService: PollService, private router: Router, private authService: AuthService) {}


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


  openPoll() {
    console.log("you pressed the 'Open poll' button")
  }

  updatePoll(updatedPoll: any) {
    if (updatedPoll) {
      // Convert the startTime to a DateTime object
      const startTime = DateTime.fromISO(updatedPoll.startTime);
      const currentTime = DateTime.local(); // Get the current time
  
      // Check if the startTime is before the current time
      if (startTime < currentTime) {
        // Handle the case where startTime is before the current time
        this.errorMessage = 'Start time cannot be in the past';
        // You can display an error message to the user or prevent form submission.
      } else {
        // Make the PUT request to update the poll
        this.pollService.updatePoll(updatedPoll).subscribe(
          (response) => {
            // Handle successful update, e.g., display a success message
            console.log('Poll updated successfully');
            // Clear the form
            this.isUpdateFormVisible[updatedPoll.id] = false;
          },
          (error) => {
            // Handle update error, e.g., display an error message
            console.error('Error updating poll:', error);
          }
        );
      }
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

      // Perform the poll deletion and remove it from the list using this.pollIdToDelete
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

  toggleUpdateFormVisibility(pollId: number) {
    this.isUpdateFormVisible[pollId] = !this.isUpdateFormVisible[pollId];
  }
}