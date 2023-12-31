import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './components/login/login.component';
import { MyPollsComponent } from './components/myPolls/myPolls.component';
import { OverviewComponent } from './components/overview/overview.component';
import { RegisterComponent } from './components/register/register.component';
import { ResultsComponent } from './components/results/results.component';
import { VoteComponent } from './components/vote/vote.component';
import { CreatePollComponent } from './components/createPoll/createPoll.component';
// import { PublicPollGuard } from './components/publicPollsGuard';
// import { PrivatePollMessageComponent } from './components/private-poll-message/private-poll-message.component';

const routes: Routes = [
    { path: '', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'overview', component: OverviewComponent },
    { path: 'login', component: LoginComponent },
    { path: 'myPolls', component: MyPollsComponent },
    { path: 'vote', component: VoteComponent },
    { path: 'results', component: ResultsComponent },
    { path: 'results/:pollId', component: ResultsComponent },
    { path: 'poll/:pollId', component: ResultsComponent },
    { path: 'createPoll', component: CreatePollComponent },
    { path: 'vote/:id', component: VoteComponent },
    // { path: 'private-poll-message', PrivatePollMessageComponent }, 
    { path: '', redirectTo: '/login', pathMatch: 'full' },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
  })
  export class AppRoutingModule {}

