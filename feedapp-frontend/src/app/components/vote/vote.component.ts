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
  errorMessage!: string;

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
          this.showVoteMessage = false;
        }, 2000);
    
        // Navigate to the results page after a brief delay
        setTimeout(() => {
          this.pollService.getResults(id).subscribe(
            (pollData) => {
              // Navigate to the results page and pass the results content
              this.router.navigate([`/poll/${id}`]);
            },
            (error) => {
              console.error('Error fetching results: ', error);
            }
          );
          // this.voteMessageTimer = setTimeout(() => {
          //   this.voteMessageTimer = false;
          // }, 2000);

        }, 1500);

  
      },
      (error) => {
        this.errorMessage = "You have already voted in this poll.";
        console.error('Error while voting: ', error)
      });
  }
}