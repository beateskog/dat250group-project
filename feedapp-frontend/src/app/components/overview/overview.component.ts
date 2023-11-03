import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PollService } from 'src/app/services/poll.service';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.css']
})
export class OverviewComponent {

  polls: any[] = []; // Replace 'any[]' with the actual type of your poll objects

  constructor(private router: Router, private pollService: PollService) {}

  ngOnInit() {
    // Fetch the list of polls from your backend service
    this.pollService.getPolls().subscribe(() => {
    });
  }

  navigateToVote(question: string) {
    // Navigate to the 'vote' page for the selected poll
    this.router.navigate(['/vote', question]);
  }

  navigateToResults(question: string) {
    // Navigate to the 'results' page for the selected poll
    this.router.navigate(['/results', question]);
  }

}