import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { PollService } from 'src/app/services/poll.service';
import { PollPrivacy } from '../pollPrivacy.enum';
import { DateTime } from 'luxon';

interface CreatePollRequest {
  question: string;
  startTime: DateTime; // Use ISO string for LocalDateTime
  endTime: DateTime;   // Use ISO string for LocalDateTime
  pollPrivacy: PollPrivacy;
}

@Component({
  selector: 'app-create-poll',
  templateUrl: './createPoll.component.html',
  styleUrls: ['./createPoll.component.css']
})

export class CreatePollComponent {
  poll = {
    question: '',
    startTime: DateTime.local(),
    endTime: DateTime.local(),
    pollPrivacy: PollPrivacy.PUBLIC
    };

  error: string = '';

  constructor(private pollService: PollService, private router: Router) {}
    createPoll() {

      const request: CreatePollRequest = {
        question: this.poll.question,
        startTime: this.poll.startTime,
        endTime: this.poll.endTime,
        pollPrivacy: this.poll.pollPrivacy === PollPrivacy.PUBLIC ? PollPrivacy.PUBLIC : PollPrivacy.PRIVATE
      };

      // const startTime = this.poll.startTime
      this.pollService.createPoll(request).subscribe(
        (response) => {
          // Handle success, e.g., navigate to "my-polls" page
          this.router.navigate(['overview']);
        },
        (error) => {
          // Handle error, e.g., display an error message
          this.error = 'Please make sure that you have filled out all the fields correctly.'
          console.error('Error creating poll:', error);
        }
      );
    }
  }


