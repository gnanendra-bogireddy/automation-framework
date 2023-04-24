@api
@paypal

  Feature: Paypal Auth API requests

    @paypaluser
    Scenario: Get user info from paypal servers
      Given I get a valid token from paypal api
      When I get a user details and validate the details
      Then I terminate the session with api