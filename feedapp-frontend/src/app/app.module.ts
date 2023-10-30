import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { AppRoutingModule } from './app-routing.module';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { MyPollsComponent } from './myPolls/myPolls.component';
import { OverviewComponent } from './overview/overview.component';
import { RegisterComponent } from './register/register.component';
import { ResultsComponent } from './results/results.component';
import { VoteComponent } from './vote/vote.component';
import { LoginComponent } from './login/login.component';
import { CreatePollComponent } from './createPoll/createPoll.component';
import { AuthService } from './auth.service';

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
