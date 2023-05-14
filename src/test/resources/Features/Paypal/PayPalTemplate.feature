@api
@paypal

Feature: Paypal Template API requests

  @paypaltemp
  Scenario: Create a template through paypal API
    Given I get a valid token from paypal api
    When I create a templates with paypal api
    Then I get the template from paypal api
    And I get all the templates from paypal api

  @paypaltempdelete
  Scenario: Delete a created template through paypal API
    Given I get a valid token from paypal api
    When I create a templates with paypal api
    Then I get the template from paypal api
    And I update the template from paypal api
    And I delete the template from paypal api
