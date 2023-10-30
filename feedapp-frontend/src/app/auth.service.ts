import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) {}

  login(credentials: { username: string, password: string }) {
    // Make an HTTP request to your Spring Boot backend for user authentication.
    // Replace 'your-api-endpoint' with the actual URL of your authentication API.
    return this.http.post('http://localhost:8080/auth/login', credentials);
  }

  register(credentials: { username: string, password: string }) {
    // Make an HTTP request to your Spring Boot backend for user authentication.
    // Replace 'your-api-endpoint' with the actual URL of your authentication API.
    return this.http.post('http://localhost:8080/auth/register', credentials);
  }


}
