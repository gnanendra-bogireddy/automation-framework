package Runner;

import io.cucumber.junit.CucumberOptions;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("Features")
@ConfigurationParameter(key =GLUE_PROPERTY_NAME, value = "StepDefs")
@ExcludeTags("ignore")
@CucumberOptions(stepNotifications = true)
public class JUnitTestRunner {

    static {



    }
}
