// // public-poll.guard.ts
// import { Injectable } from '@angular/core';
// import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
// import { Observable } from 'rxjs';
// import { PollService } from './poll.service';
// // 
// @Injectable({
//   providedIn: 'root'
// })
// export class PublicPollGuard implements CanActivate {

//   constructor(private pollService: PollService, private router: Router) {}

//   canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | Promise<boolean> | boolean {
//     const pollId = +route.params['id'];

//     // Use your PollService or an appropriate method to check if the poll is public
//     const isPublic = this.pollService.isPollPublic(pollId);

//     if (isPublic) {
//       return true; // Allow access to the vote page
//     } else {
//       // Redirect to a message page for private polls
//       this.router.navigate(['/private-poll-message']);
//       return false; // Do not allow access to the vote page
//     }
//   }
// }