import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PollPrivacy } from '../components/pollPrivacy.enum';
import { DateTime } from 'luxon';
import { AuthService } from './auth.service';
import { Observable, catchError, map, of, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class PollService {

  public noPolls: boolean = false;
  public confirmationMessage: string = '';
  public isConfirmationDialogOpen: boolean = false;
  userPolls: any[] = [];
  publicPolls: any[] = [];
  poll!: any;
  privacy!: any;


  constructor(private http: HttpClient, private authService: AuthService) {}

  createPoll(credentials: {question: string, startTime: DateTime, endTime: DateTime, pollPrivacy: PollPrivacy}) {
    const request = {
      ...credentials,
      startTime: credentials.startTime.toString(),
      endTime: credentials.endTime.toString()
    };
    
    const authToken = this.authService.getToken();
    // Create the HttpHeaders with the authorization token
    const headers = new HttpHeaders({
    Authorization: `Bearer ${authToken}`
    });
    return this.http.post(`http://localhost:8080/poll`, request, {headers});
  }

  updatePoll(credentials: {pollPrivacy: PollPrivacy, startTime: DateTime, endTime: DateTime, id: number, pollOwnerId: number}) {
    const authToken = this.authService.getToken();
    const headers = new HttpHeaders({
    Authorization: `Bearer ${authToken}`
    })
    const request = {
      id: credentials.id,
      pollOwnerId: credentials.pollOwnerId,
      pollPrivacy: credentials.pollPrivacy,
      startTime: credentials.startTime.toString(),
      endTime: credentials.endTime.toString(),
    };
    return this.http.put(`http://localhost:8080/poll`, request, {headers});
  }


  deletePoll(id: number) {
    const authToken = this.authService.getToken();
    const headers = new HttpHeaders({
    Authorization: `Bearer ${authToken}`
    });
    return this.http.delete(`http://localhost:8080/poll/${id}`, {headers});
  }

  navigateToVote() {
    const authToken = this.authService.getToken();
    const headers = new HttpHeaders({
    Authorization: `Bearer ${authToken}`
    });

    return this.http.get(`http://localhost:8080/vote`, {headers});
  }
  
  getPolls() {
    const authToken = this.authService.getToken();
    const headers = new HttpHeaders({
    Authorization: `Bearer ${authToken}`
    });
    return this.http.get(`http://localhost:8080/poll/all`, {headers});
  }

  getUserPolls(): Observable<any> {

    const authToken = this.authService.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${authToken}`
    });
  
    return this.http.get(`http://localhost:8080/poll/owner`, { headers }).pipe(
      catchError((error) => {
        if (error.status === 404) {
          // Handle the case when the user has no polls
          this.noPolls = true;
          this.confirmationMessage = "It looks like you haven't made any polls yet. Create your first poll!";
          return of([]);
        } else {
          return (error);
        }
      })
    );
  }

  getPublicPolls() {
    return this.http.get('http://localhost:8080/poll/all/public');
  }

  searchPollsById(id: number) {
    const authToken = this.authService.getToken();
    const headers = new HttpHeaders({
    Authorization: `Bearer ${authToken}`
    });
    return this.http.get(`http://localhost:8080/poll/${id}`);
  }

  searchPublicPollsById(id: number) {
      return this.http.get(`http://localhost:8080/poll/public/${id}`);
  }

  isPollActive(poll: any): boolean {
    const currentTime = DateTime.local(); // Get the current time
    const endTime = DateTime.fromISO(poll.endTime); // Parse poll's end time
    return endTime > currentTime; // Return true if poll is open, false if closed
  }

  isPollPublic(poll: any) {
    console.log(this.poll)
    this.privacy = poll.pollPrivacy
    return this.privacy == "PUBLIC";
  }

  getEndedPollResults(pollId: number) {
    const authToken = this.authService.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${authToken}`,
    });
    return this.http.get(`http://localhost:8080/result/${pollId}`, {headers});
  }

  getResults(pollId: number) {
    const authToken = this.authService.getToken();
    const headers = new HttpHeaders({
      Authorization: `Bearer ${authToken}`,
    });
    return this.http.get(`http://localhost:8080/poll/${pollId}`, {headers});
  }

}

