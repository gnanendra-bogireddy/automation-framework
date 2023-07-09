package PostmanEchoAPI;

import TestHelper.TestHelper;
import Util.Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.authentication.OAuthSignature;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostmanEcho {

    private TestHelper testHelper = new TestHelper();
    private ObjectMapper objectMapper = new ObjectMapper();
    private static String ECHO_BASE_URI = Util.loadClassPathProperty("application.properties", "postman.echo.api");
    private static String ECHO_USERNAME = Util.loadClassPathProperty("application.properties", "postman.echo.username");
    private static String ECHO_PASSWORD = Util.loadClassPathProperty("application.properties", "postman.echo.password");
    private static String ECHO_CONSUMER_KEY = Util.loadClassPathProperty("application.properties", "postman.echo.consumer.key");
    private static String ECHO_CONSUMER_SECRET = Util.loadClassPathProperty("application.properties", "postman.echo.consumer.secret");
    private static String BASIC_AUTH = "basic-auth";
    private static String DIGEST_AUTH = "digest-auth";
    private static String OAUTH = "oauth1";

    private static Logger logger = LoggerFactory.getLogger(PostmanEcho.class);

    public void iUseBasicAuth() {

        Response response = RestAssured.given()
                .auth()
                .preemptive()
                .basic(ECHO_USERNAME, ECHO_PASSWORD)
                .baseUri(ECHO_BASE_URI)
                .accept(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .get(BASIC_AUTH);

        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("Response code is : "+response.getStatusCode());
        logger.info("Response body is : "+response.getBody().asPrettyString());
        Assert.assertEquals(response.getBody().jsonPath().getBoolean("authenticated"), true);
    }

    public void iUseDigestAuth() {

        Response response = RestAssured.given()
                .auth()
                .digest(ECHO_USERNAME, ECHO_PASSWORD)
                .baseUri(ECHO_BASE_URI)
                .accept(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .get(DIGEST_AUTH);

        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("Response code is : "+response.getStatusCode());
        logger.info("Response body is : "+response.getBody().asPrettyString());
        Assert.assertEquals(response.getBody().jsonPath().getBoolean("authenticated"), true);
    }

    public void iUseOAuth() {

        Response response = RestAssured.given()
                .auth()
                .oauth(ECHO_CONSUMER_KEY, ECHO_CONSUMER_SECRET, "", "", OAuthSignature.HEADER)
                .baseUri(ECHO_BASE_URI)
                .accept(ContentType.JSON)
                .when()
                .accept(ContentType.JSON)
                .get(OAUTH);

        Assert.assertEquals(response.getStatusCode(), 200);
        logger.info("Response code is : "+response.getStatusCode());
        logger.info("Response body is : "+response.getBody().asPrettyString());
        Assert.assertEquals(response.getBody().jsonPath().getString("status"), "pass");
    }
}
