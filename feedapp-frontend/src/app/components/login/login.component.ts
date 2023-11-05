import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service'; // You'll need to create an AuthService for handling authentication.

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
  })

  export class LoginComponent {
    loginForm: { username: string, password: string } = { username: '', password: '' };
    error: string = '';
  
    constructor(private authService: AuthService, private router: Router) { }
  
    login() {
      this.authService.login(this.loginForm).subscribe(
        (response) => {
          this.authService.setToken(response);
          this.router.navigate(['/overview']);
        },
        (error) => {
          this.error = "The username or password is incorrect";
          console.error("Login failed:", error);
        }
      );
    }
  }