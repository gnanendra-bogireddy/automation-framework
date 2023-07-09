@api
@paypal

Feature: Paypal Invoice API requests

  @paypalinvoice
  Scenario: Create invoices from paypal api
    Given I get a valid token from paypal api
    When I generate invoice number with paypal api
    And I generate draft invoice with paypal api