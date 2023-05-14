@api
@paypal

Feature: Paypal Order API requests

  @paypalorder
  Scenario: Order through paypal API
    Given I get a valid token from paypal api
    When I submit an order with paypal
    Then I get the order details with orderid

  @updateorder
  Scenario: Order update through paypal API
    Given I get a valid token from paypal api
    When I submit an order with paypal
    Then I update the paypal order
    And I get the order details with orderid

  @ignore
  @wip
  @authorizeorder
  Scenario: Order validation through paypal API
    Given I get a valid token from paypal api
    When I submit an order with paypal
    And I confirm an oder with paypal
    Then I authorize order with paypalapi
    And I capture order with paypalapi