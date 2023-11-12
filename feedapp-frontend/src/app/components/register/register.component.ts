
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

  register() {
    // Check if the password and confirm password match
    if (this.registrationForm.password !== this.registrationForm.confirmPassword) {
      this.error = 'Password and confirm password do not match.';
      return;
    }
    this.authService.register({username: this.registrationForm.username, password: this.registrationForm.password}).subscribe(
        (response) => {
          this.authService.setToken(response);
          this.router.navigate(['/overview']);
          console.log("success");
        },
    );
  }
}