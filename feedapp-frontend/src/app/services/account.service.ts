import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface Account {
  id: number;
  username: string;
  role: string;
  numberOfpolls: number;
  polls: number[];
}


@Injectable({
  providedIn: 'root'
})

export class AccountService {

  private username: string = '';

  constructor(private http: HttpClient) { }

  setUsername(username: string) {
    this.username = username;
  }

  getUsername(): string {
    return this.username;
  }

  // getAccountDetails(): Account {

  //   return 
  // }

  getAccountByUsername(username: string): Observable<Account> {
    return this.http.get<Account>(`http://localhost:8080/account/username/${username}`);
  }

  getAccountById(id: number) {
    return this.http.get(`http://localhost:8080/account/id/${id}`);
  }
}
