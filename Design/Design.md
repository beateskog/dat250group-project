# Design document

<p align="center">
  <img height=400 width=300 src="diagrams/Figure-logo.png">
</p>

### Group members

Beate Skogvik, Rita Haveland Borlaug, and Sunniva Storetvedt Lothe

### Appassignment A

- Use case diagrams
- A domain model for the application
- Application flow diagrams for the front-end
- Mock-up user screens for the front-end
- A system-specific architectural diagram

# The FeedApp Assignment

FeedApp is an IoT-cloud software system in which users can provide feedback through binary yes/no votes on polls, either via a web-client front-end or via an IoT-voting device.

## Use cases and use case diagrams

Use cases for this application includes:
- Manage poll
- Create poll
- Close poll
- Choose poll
- Vote
- See results
- Create account
- Login
- Log out

Our system recognizes four primary actors: admin, user, voter and IoT device. All four roles can decide to vote on polls. An actor can choose a poll three different ways:
1. Choosing a poll amongst the public polls that has been created in the FeedApp.
2. Via a unique number that is automatically generated for each poll. These polls can either be public or private.
3. Via a url that is automatically generated for each poll. These polls can also be either public or private.

The difference between a user and a voter is that a voter cannot create polls, but the user can. The difference between user and admin is that the admin can manage the polls. This includes the option to delete polls and remove voters from the polls.

The figure below displays the use case diagram for our application:

<p align="center">
  <img  width=400 src="diagrams/Figure-Use-case-diagram.png">
</p>


## Domain Model 
![Domain Model](diagrams/Figure-Domain-model.png)

## Example of a database with test data

![Account](./assets/tables//Account.png)
![Poll](./assets/tables/Poll.png)

![Vote](./assets/tables/Vote.png)

![Account_Poll](./assets/tables/Account_Poll.png)
![Poll_Vote](./assets/tables/Poll_Vote.png)



## Application flow diagram (front-end)

The application flow diagram for the front-end web-client:

<p align="center">
  <img height=600 width=700 src="diagrams/Figure-Application-flow-diagram.png">
</p>


## Mock-up user screens (front-end)

The layout for the web-client:

![Figure-Mock-up-user-screens](diagrams/Figure-Mock-up-user-screens.png)


## System-specific architectural diagram

The system consists of the following elements:

<p align="center">
  <img height=450 width=800 src="diagrams/Figure-System-specific-architecture-diagram.png">
</p>