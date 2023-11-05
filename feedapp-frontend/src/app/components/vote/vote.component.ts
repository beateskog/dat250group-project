import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PollService } from 'src/app/services/poll.service';
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

  // voteInfo: {vote: boolean, pollId: any, votingPlatform: VotingPlatform} = {vote: false, pollId: 1, votingPlatform: VotingPlatform.WEB};
  question: string = '';
  pollId!: number;
  showVoteMessage: boolean = false;
  voteMessageTimer: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private voteService: VoteService,
    private pollService: PollService
  ) {}

  ngOnInit() {
    this.route.params.subscribe((params) => {
      // Retrieve the question from route parameters
      const pollId = +params['id'];

      this.pollService.searchPollsById(pollId).subscribe((poll: any) => {
        // Set the question and end time
        this.question = poll.question;
        // this.endTime = poll.endTime;
        this.pollId = pollId;
      });

    });
  }

  vote(id: number, voteValue: boolean) {
    // this.pollId = id;
    const voteInfo = {
      vote: voteValue,
      pollId: id,
      votingPlatform: VotingPlatform.WEB,
    };
    // Send the boolean vote value to the backend (True for Yes, False for No)
    this.voteService.vote(voteInfo).subscribe(
      (response) => {
        console.log("Vote submitted successfully");
        this.showVoteMessage = true;
        console.log(this.showVoteMessage);
        setTimeout(() => {
          // this.voteMessageTimer = false;
          this.router.navigate(['/results']);
        }, 2000);

        this.showVoteMessage = false;
        // Navigate to the results page after voting
        // this.router.navigate(['/results']);
      },
      (error) => {
        console.error('Error while voting: ', error)
      });
  }
}