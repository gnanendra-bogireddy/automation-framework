@api
@paypal

Feature: Paypal Order API requests

  @paypalorder
  Scenario: Order through paypal API
    Given I get a valid token from paypal api
    When I submit an order with paypal