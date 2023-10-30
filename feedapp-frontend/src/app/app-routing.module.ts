import {NgModule} from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { OverviewComponent } from './overview/overview.component';
import { MyPollsComponent } from './myPolls/myPolls.component';
import { VoteComponent } from './vote/vote.component';
import { ResultsComponent } from './results/results.component';


const routes: Routes = [
    { path: '', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'overview', component: OverviewComponent },
    { path: 'login', component: LoginComponent},
    { path: 'myPolls', component: MyPollsComponent},
    { path: 'vote', component: VoteComponent},
    { path: 'results', component: ResultsComponent},
    { path: '', redirectTo: '/login', pathMatch: 'full' },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
  })
  export class AppRoutingModule {}

