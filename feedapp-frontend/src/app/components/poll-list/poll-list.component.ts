import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-poll-list',
  templateUrl: './poll-list.component.html',
  styleUrls: ['./poll-list.component.css']
})
export class PollListComponent {

  @Input() polls: any[] = []; // Replace 'any[]' with the actual type of your poll objects

  constructor(private router: Router) {}

  openPoll(poll: any) {
    if (poll.isActive) {
      // Poll is active, navigate to the "vote" page
      this.router.navigate(['/vote', poll.pollID]);
    } else {
      // Poll has ended, navigate to the "results" page
      this.router.navigate(['/results', poll.pollID]);
    }
  }

}
