package StepDefs;

import PayPal.PayPalAPI;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class PayPalStepDefs {

    private  PayPalAPI payPalAPI = new PayPalAPI();

    @Given("^I get a valid token from paypal api$")
    public  void iGetAValidToken() {
        payPalAPI.iGetPaypalToken();
    }
    @When("^I get a user details and validate the details$")
    public void iGetUserDetails() {
        payPalAPI.iGetUserInfo();

    }
    @Then("^I terminate the session with api$")
    public  void iTerminateSession() {
        payPalAPI.iTerminateAccessToken();
    }

    @When("^I submit an order with paypal$")
    public void iSubmitAnOrder() {
        payPalAPI.iSubmitAnOrder();
    }
}
