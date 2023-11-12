import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app-routing.module';
import { JwtInterceptor } from './components/interceptors/jwt.interceptor';

import { AppComponent } from './app.component';
import { CreatePollComponent } from './components/createPoll/createPoll.component';
import { LoginComponent } from './components/login/login.component';
import { MyPollsComponent } from './components/myPolls/myPolls.component';
import { OverviewComponent } from './components/overview/overview.component';
import { RegisterComponent } from './components/register/register.component';
import { ResultsComponent } from './components/results/results.component';
import { VoteComponent } from './components/vote/vote.component';
import { AuthService } from './services/auth.service';
import { PollListComponent } from './components/poll-list/poll-list.component';
// import { PollComponent } from './components/poll/poll.component';

@NgModule({
  declarations: [
    AppComponent,
    MyPollsComponent,
    OverviewComponent,
    RegisterComponent,
    ResultsComponent,
    VoteComponent,
    LoginComponent,
    CreatePollComponent,
    PollListComponent,
    // PollComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    RouterModule,
    HttpClientModule
  ],
  providers: [AuthService, {
    provide: HTTP_INTERCEPTORS,
    useClass: JwtInterceptor,
    multi: true,
  } ],
  bootstrap: [AppComponent]
})
export class AppModule { }
