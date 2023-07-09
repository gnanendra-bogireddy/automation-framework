package StepDefs;

import PostmanEchoAPI.PostmanEcho;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;

public class PostmanAPIStepDefs {

     PostmanEcho postmanEcho = new PostmanEcho();

    @Given("I use basic auth in postman")
    public void iUseBasicAuth() {
        postmanEcho.iUseBasicAuth();
    }

    @And("I use digest auth in postman")
    public void iUseDigestAuthInPostman() {
        postmanEcho.iUseDigestAuth();
    }

    @And("I use oauth in postman")
    public void iUseOAuthInPostman() {
        postmanEcho.iUseOAuth();
    }
}
