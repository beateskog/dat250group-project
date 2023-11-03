import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { VoteService } from 'src/app/services/vote.service';

export enum VotingPlatform {
  IoT = 'IoT',
  WEB = 'WEB'
}


@Component({
  selector: 'app-vote',
  templateUrl: './vote.component.html',
  styleUrls: ['./vote.component.css']
})
export class VoteComponent implements OnInit {

  voteInfo: {vote: boolean, pollId: number, votingPlatform: VotingPlatform} = {vote: false, pollId: 1, votingPlatform: VotingPlatform.WEB};
  question: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private voteService: VoteService
  ) {}

  ngOnInit() {
    this.route.params.subscribe((params) => {
      // Retrieve the question from route parameters
      this.question = params['question'];
    });
  }

  vote() {
    // Send the boolean vote value to the backend (True for Yes, False for No)
    this.voteService.vote(this.voteInfo).subscribe(
      (response) => {
      // Navigate to the results page after voting
      this.router.navigate(['/vote', this.question]);
    });
  }

}