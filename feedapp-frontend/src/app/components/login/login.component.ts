import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service'; // You'll need to create an AuthService for handling authentication.
import { AccountService } from 'src/app/services/account.service';
import { Account } from 'src/app/services/account.service';
import { OverviewComponent } from '../overview/overview.component';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
  })

  export class LoginComponent {
    loginForm: { username: string, password: string } = { username: '', password: '' };
    error: string = '';

    account: any;
    username!: string;
  
    constructor(private authService: AuthService, private accountService: AccountService, private router: Router) { }
  
    login() {
      this.authService.login(this.loginForm).subscribe(
        (response) => {
          localStorage.setItem('loggedInUsername', this.loginForm.username);
          this.authService.setToken(response);
          console.log("account", this.username);
          console.log("username", this.loginForm.username);
          this.accountService.setUsername(this.loginForm.username);
          this.router.navigate(['/overview']);
        },
        (error: any) => {
          this.error = "The username or password is incorrect";
          console.error("Login failed:", error);
        }
      );
    }

    goToPolls() {
      // Redirect to the overview page without user authentication
      this.router.navigate(['/overview'], { queryParams: { publicPollsOnly: true } });
    }


  }