import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { CreatePollComponent } from './components/createPoll/createPoll.component';
import { LoginComponent } from './components/login/login.component';
import { MyPollsComponent } from './components/myPolls/myPolls.component';
import { OverviewComponent } from './components/overview/overview.component';
import { RegisterComponent } from './components/register/register.component';
import { ResultsComponent } from './components/results/results.component';
import { VoteComponent } from './components/vote/vote.component';
import { AuthService } from './services/auth.service';

@NgModule({
  declarations: [
    AppComponent,
    MyPollsComponent,
    OverviewComponent,
    RegisterComponent,
    ResultsComponent,
    VoteComponent,
    LoginComponent,
    CreatePollComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    RouterModule,
    HttpClientModule
  ],
  providers: [AuthService],
  bootstrap: [AppComponent]
})
export class AppModule { }
