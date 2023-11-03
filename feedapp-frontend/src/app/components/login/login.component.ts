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
      // You should implement the login logic, typically involving an HTTP request to your Spring Boot API.
      this.authService.login(this.loginForm).subscribe(
        (response) => {
          this.authService.setToken(response);
          // Successful login; you can redirect the user to another page, e.g., their dashboard.
          this.router.navigate(['/overview']);
          console.log("yes");
        },
        (error) => {
          // If there is an error, set the error message to inform the user.
          this.error = "The username or password is incorrect";
          console.error("Login failed:", error);
        }
      );
    }
  }