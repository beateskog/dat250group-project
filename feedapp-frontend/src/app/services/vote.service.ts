import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { VotingPlatform } from '../components/vote/vote.component';

@Injectable({
  providedIn: 'root'
})
export class VoteService {

  constructor(private http: HttpClient) {}

  vote(credentials: { vote: boolean, pollId: number, votingPlatform: VotingPlatform}) {
    // Send the boolean vote value to the backend (True for Yes, False for No)
    return this.http.post('http://localhost:8080/vote', credentials);
  }

  getResults() {
    // Send a request to retrieve poll results based on the question
    return this.http.get('http://localhost:8080/vote');
  }

}
