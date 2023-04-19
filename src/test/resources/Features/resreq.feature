@api
@resreq

  Feature: Resreq api tests

    @createuser
    Scenario: Post request for creating a user and modifying the user
      Given I create a user with name gnanendra and role admin
      When I modify user gnanendra role as root

    @getuser
    Scenario: Get request for created user
      Given I get a user with from resreq

    @deleteuser
    Scenario: Delete request for created user
      Given I create a user with name gnanendra and role admin
      When I delete a user with given id

    @patchreq
    Scenario: Patch request for modifying a user
      Given I create a user with name gnanendra and role admin
      When I upate created user with name bogireddy and role packman
      
    @delayedresponse
    Scenario: Delayed get response
      Given I get a delayed get response of 5

    @register
    Scenario Outline: User register and login
      Given I register user with email <emailId> and password <password>
      When I register with email <emailId> and register was unsuccessful

      Examples:
        | emailId            | password |
        | eve.holt@reqres.in | pistol   |

    @login
    Scenario Outline: User able to login
      Given I login user with email <emailId> and password <password>
      When I login with email <emailId> and login was unsuccessful

      Examples:
        | emailId            | password   |
        | eve.holt@reqres.in | cityslicka |


