import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PollPrivacy } from '../components/pollPrivacy.enum';
import { DateTime } from 'luxon';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})

export class PollService {

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
  
  getPolls() {
    const authToken = this.authService.getToken();

    // Create the HttpHeaders with the authorization token
    const headers = new HttpHeaders({
    Authorization: `Bearer ${authToken}`
    });
    return this.http.get(`http://localhost:8080/poll/all`, {headers});
  }


  getUserPolls() {

    const authToken = this.authService.getToken();

    // Create the HttpHeaders with the authorization token
    const headers = new HttpHeaders({
    Authorization: `Bearer ${authToken}`
    });
    return this.http.get(`http://localhost:8080/poll/owner`)

  }
}

