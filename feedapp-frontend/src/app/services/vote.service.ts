import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { VotingPlatform } from '../components/vote/vote.component';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class VoteService {

  constructor(private http: HttpClient, private authService: AuthService) {}

  vote(credentials: { vote: boolean, pollId: number, votingPlatform: VotingPlatform}) {
    // Send the boolean vote value to the backend (True for Yes, False for No)
    return this.http.post('http://localhost:8080/vote', credentials);
  }

}
