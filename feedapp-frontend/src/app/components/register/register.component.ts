
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  registrationForm: { username: string, password: string, confirmPassword: string } = {
    username: '',
    password: '',
    confirmPassword: ''
  };
  error: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  // Also add an error message if the username is already in use.

  register() {
    // Check if the password and confirm password match
    if (this.registrationForm.password !== this.registrationForm.confirmPassword) {
      this.error = 'Password and confirm password do not match.';
      return;
    }
    this.authService.register({username: this.registrationForm.username, password: this.registrationForm.password}).subscribe(
        (response) => {
          this.authService.setToken(response);
          // Successful login; you can redirect the user to another page, e.g., their dashboard.
          this.router.navigate(['/overview']);
          console.log("success");
        },
    );
    // Perform user registration logic here.
    // You can make an HTTP request to your Spring Boot API to create a new user.

    // Assuming registration is successful, navigate to the "overview" page.
  }
}