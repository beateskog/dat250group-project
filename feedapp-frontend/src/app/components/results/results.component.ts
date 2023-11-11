import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { PollService } from 'src/app/services/poll.service';
import { VoteService } from 'src/app/services/vote.service';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit {
  pollData!: any;
  pollId!: number;
  totalVotes!: number;
  yesVotes!: number;
  noVotes!: number;

  constructor(private route: ActivatedRoute, private pollService: PollService) {}

  ngOnInit() {
    this.route.paramMap.subscribe((params: ParamMap) => {
      const pollId = params.get('pollId');
  
      if (pollId !== null) {
        this.pollId = +pollId;
  
        // Fetch poll data based on pollId using our PollService method
        this.pollService.getResults(this.pollId).subscribe((data) => {
          this.pollData = data;
          // Additional code to process the poll data if needed
        });
      } else {
        // Handle the case where 'pollId' is not present in the route parameters
        console.error('Poll ID is missing from route parameters.');
      }
    });
  }
}