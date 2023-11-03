import { Component } from '@angular/core';
import { PollService } from 'src/app/services/poll.service';
import { PollListComponent } from '../poll-list/poll-list.component';
import { DateTime } from 'luxon';
import { PollPrivacy } from '../pollPrivacy.enum';
import { Router } from '@angular/router';

interface PollDTO{
  id: number;
  pollUrl: string;
  pollPin: number;
  question: string;
  startTime: DateTime;
  endTime: DateTime;
  PollPrivacy: PollPrivacy;
  pollOwner: string;
  pollOwnerId: number;
  totalVotes: number;
  yesVotes: number;
  noVotes: number;
}


@Component({
  selector: 'app-my-polls',
  templateUrl: './myPolls.component.html',
  styleUrls: ['./myPolls.component.css']
})


export class MyPollsComponent {
  // pollDTO = {
  //   id!: '',
  //   pollUrl!: '',
  //   pollPin: '';
  //   question: '';
  //   startTime: '';
  //   endTime: '';
  //   PollPrivacy: '';
  //   pollOwner: '';
  //   pollOwnerId: '';
  //   totalVotes: '';
  //   yesVotes: '';
  //   noVotes: '';
  // }

  // const pollObject: PollDTO = {
  //   question: this.question,
  //   startTime: this.poll.startTime,
  //   endTime: this.poll.endTime,
  //   pollPrivacy: this.poll.pollPrivacy === PollPrivacy.PUBLIC ? PollPrivacy.PUBLIC : PollPrivacy.PRIVATE
  // };


openPoll(_t6: any) {
  console.log("you pressed the 'Open poll' button")
// throw new Error('Method not implemented.');
}

  userPolls: any[] = []; // Replace 'any[]' with the actual type of your poll objects

  constructor(private pollService: PollService, private router: Router) {}

  redirectToPoll(poll: any): void {
    if (poll.isStillActive) {
      // Redirect to the "poll" page for an active poll
      this.router.navigate(['/poll', poll.pollId]);
    } else {
      // Redirect to the "results" page for a closed poll
      this.router.navigate(['/results', poll.pollId]);
    }
  }

  ngOnInit() {
    // Fetch the list of user's polls from the backend based on the username
    const username = "test"; // Replace with the actual username
    this.pollService.getUserPolls().subscribe({
      next: (polls:any) => {this.userPolls = polls;}
        ,
      error: () => {}
  });

  
  }
}