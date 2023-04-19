package Runner;

import io.restassured.RestAssured;
import org.junit.platform.suite.api.*;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("Features")
@ConfigurationParameter(key =GLUE_PROPERTY_NAME, value = "StepDefs")
@ExcludeTags("ignore")
public class JUnitTestRunner {

    static {



    }
}
