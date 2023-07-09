package StepDefs;

import PayPal.PayPalAPI;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;
import java.net.URISyntaxException;

public class PayPalStepDefs {

    private  PayPalAPI payPalAPI = new PayPalAPI();

    @Given("^I get a valid token from paypal api$")
    public void iGetAValidToken() { payPalAPI.iGetPaypalToken(); }

    @When("^I get a user details and validate the details$")
    public void iGetUserDetails() { payPalAPI.iGetUserInfo(); }

    @Then("^I terminate the session with api$")
    public void iTerminateSession() {
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
    public void iUpdateThePaypalOrder() throws JsonProcessingException { payPalAPI.iUpdateTheOrderDetails(); }

    @Then("I authorize order with paypalapi")
    public void iAuthorizeOrderWithPaypalapi() {
        payPalAPI.iAuthorizeTheOrderDetails();
    }

    @And("I capture order with paypalapi")
    public void iCaptureOrderWithPaypalapi() {
        payPalAPI.iCaptureTheOrderDetails();
    }

    @And("I confirm an oder with paypal")
    public void iConfirmAnOderWithPaypal() throws JsonProcessingException { payPalAPI.iConfirmTheOrderDetails(); }

    @When("I create a templates with paypal api")
    public void iCreateATemplatesWithPaypalApi() throws IOException, URISyntaxException { payPalAPI.iCreateATemplateWithPaypal(); }

    @Then("I get all the templates from paypal api")
    public void iGetAllTheTemplatesFromPaypalApi() { payPalAPI.iGetAllTemplatesWithPaypal(); }

    @Then("I get the template from paypal api")
    public void iGetTheTemplateFromPaypalApi() {
        payPalAPI.iGetTemplateWithPaypal();
    }

    @Then("I update the template from paypal api")
    public void iUpdateTheTemplateFromPaypalApi() {
        payPalAPI.iUpdateTemplateWithPaypal();
    }

    @Then("I delete the template from paypal api")
    public void iDeleteTheTemplateFromPaypalApi() {
        payPalAPI.iDeleteTemplateWithPaypal();
    }

    @When("I generate invoice number with paypal api")
    public void iGenerateTheInvoiceNumberFromPaypalApi() { payPalAPI.iGenerateInvoiceNumberWithPaypal(); }

    @When("I generate draft invoice with paypal api")
    public void iGenerateTheDraftInvoiceFromPaypalApi() throws IOException, URISyntaxException { payPalAPI.iCreateADraftInvoiceWithPaypal(); }
}
