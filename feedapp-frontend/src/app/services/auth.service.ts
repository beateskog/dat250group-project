import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  getToken(): string | null {
    return localStorage.getItem('jwt');
  }

  setToken(response: any): void {
    const jwtToken = response.jwt;
    localStorage.setItem('jwt', jwtToken);
  }

  removeToken(): void {
    localStorage.removeItem('jwt');
  }

  constructor(private http: HttpClient) {}

  login(credentials: { username: string, password: string }) {
    // Make an HTTP request to your Spring Boot backend for user authentication.
    return this.http.post('http://localhost:8080/auth/login', credentials);
  }

  register(credentials: { username: string, password: string }) {
    // Make an HTTP request to your Spring Boot backend for user authentication.
    return this.http.post('http://localhost:8080/auth/register', credentials);
  }
}
