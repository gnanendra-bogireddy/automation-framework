package StepDefs;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import resreq.ResreqAPI;

public class ResReqStepDefs {

    private ResreqAPI resreqAPI = new ResreqAPI();

    @Given("I create a user with name {} and role {}")
    public void iCreateAUser(String user, String role) {
        resreqAPI.createUser(user, role);
    }

    @When("I modify user {} role as {}")
    public void iModifyUserRole(String user, String role) {
        resreqAPI.modifyUser(user, role);
    }

    @Then("I get a user with from resreq")
    public void iGetAUserWithFromResreq() {
        resreqAPI.getUser();
    }

    @When("I delete a user with given id")
    public void iDeleteAUserWithGivenId() {
        resreqAPI.deleteUser();
    }

    @When("I upate created user with name {} and role {}")
    public void iUpateCreatedUserWithNameAndRole(String user, String role) {
        resreqAPI.patchUser(user, role);
    }

    @Given("I get a delayed get response of {int}")
    public void iGetADelayedGetResponse(int delay) {
        resreqAPI.delayedResponse(delay);
    }

    @Given("I register user with email {} and password {}")
    public void iRegisteraUserWithEmailAndPassword(String email, String password) {
        resreqAPI.createUserWithEmailAndPassword(email, password);
    }

    @When("I register with email {} and register was unsuccessful")
    public void iRegisterWithEmailAndRegisterWasUnsuccessful(String email) {
        resreqAPI.createUserWithEmail(email);
    }

    @Given("I login user with email {} and password {}")
    public void iLoginUserWithEmailAndPassword(String email, String pass) {
        resreqAPI.loginUserWithEmailAndPassword(email, pass);
    }

    @When("I login with email {} and login was unsuccessful")
    public void iLoginWithEmailAndLoginWasUnsuccessful(String email) {
        resreqAPI.loginUserWithEmail(email);
    }
}
