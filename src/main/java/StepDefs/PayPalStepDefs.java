package StepDefs;

import PayPal.PayPalAPI;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
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

    @Then("I get the order details with orderid")
    public void iGetTheOrderDetaisWithOrderid() {
        payPalAPI.iGetTheOrderDetails();
    }

    @Then("I update the paypal order")
    public void iUpdateThePaypalOrder() throws JsonProcessingException {
        payPalAPI.iUpdateTheOrderDetails();
    }

    @Then("I authorize order with paypalapi")
    public void iAuthorizeOrderWithPaypalapi() {
        payPalAPI.iAuthorizeTheOrderDetails();
    }

    @And("I capture order with paypalapi")
    public void iCaptureOrderWithPaypalapi() {
        payPalAPI.iCaptureTheOrderDetails();
    }

    @And("I confirm an oder with paypal")
    public void iConfirmAnOderWithPaypal() throws JsonProcessingException {
        payPalAPI.iConfirmTheOrderDetails();
    }
}
