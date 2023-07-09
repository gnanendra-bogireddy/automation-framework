@api
@postman

  Feature: Postman API requests

    @echoauth
    Scenario: Postman auth methods
      Given I use basic auth in postman
      And I use digest auth in postman
      And I use oauth in postman




