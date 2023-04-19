package resreq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import resreq.model.CreatedResponse;
import resreq.model.GetResponse;
import resreq.model.User;

public class ResreqAPI {

    private static final String CRAEATE_REQUEST = "api/users";
    private static final String MODIFY_REQUEST = "api/users/%s";
    private static final String GET_REQUEST = "api/users/2";
    private static  final String DELETE_REQUEST = "api/users/%s";
    private static  final String PATCH_REQUEST = "api/users/%s";
    private static  final String DELAY_REQUEST = "api/users?delay=%s";
    private static final String REGISTER_REQUEST = "api/register";
    private static final String  LOGIN_REQUEST = "api/login";
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final String BASE_PATH = Util.loadClassPathProperty("application.properties", "resreq.base.uri");
    private static String  payload;
    private static String  id;
    private static CreatedResponse createdResponse = new CreatedResponse();

    private static Logger logger = LoggerFactory.getLogger(ResreqAPI.class);

    public String createUserPayload(String user, String role) {

        User createUserPayload = User.builder()
                .name(user)
                .job(role)
                .build();
        try {
            payload = objectMapper.writeValueAsString(createUserPayload);
            logger.debug("Payload is : " + payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return payload;
    }

    public void createUser(String user, String role) {
        String payload = this.createUserPayload(user, role);
        Response response = RestAssured.given()
                .baseUri(BASE_PATH)
                .accept(ContentType.JSON)
                .basePath(CRAEATE_REQUEST)
                .body(payload)
                .accept(ContentType.JSON)
                .post();
        Assert.assertEquals(response.getStatusCode(), 201);
        logger.debug("Response body is : " + response.asPrettyString());
        try {
            objectMapper.readValue(response.getBody().asPrettyString(), CreatedResponse.class);
            id = createdResponse.getId();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void modifyUser(String user, String role) {
        String payload = this.createUserPayload(user, role);
        Response response = RestAssured.given()
                .baseUri(BASE_PATH)
                .accept(ContentType.JSON)
                .basePath(String.format(MODIFY_REQUEST, id))
                .body(payload)
                .accept(ContentType.JSON)
                .put();
        Assert.assertEquals(response.getStatusCode(), 200);
        logger.debug("Response body is : " + response.asPrettyString());
    }

    public void getUser() {
        Response response = RestAssured.given()
                .baseUri(BASE_PATH)
                .accept(ContentType.JSON)
                .get(GET_REQUEST);
        Assert.assertEquals(response.getStatusCode(), 200);
        logger.debug("Response body is : " + response.asPrettyString());
        try {
            objectMapper.readValue(response.getBody().asPrettyString(), GetResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteUser() {
        Response response = RestAssured.given()
                .baseUri(BASE_PATH)
                .accept(ContentType.JSON)
                .delete(String.format(DELETE_REQUEST, id));
        Assert.assertEquals(response.getStatusCode(), 204);
        logger.debug("Response code is : " + response.getStatusCode());
    }

    public void patchUser(String user, String role)  {
        String payload = this.createUserPayload(user, role);
        Response response = null;
        try {
            response = RestAssured.given()
                    .baseUri(BASE_PATH)
                    .accept(ContentType.JSON)
                    .body(objectMapper.writeValueAsString(payload))
                    .patch(String.format(PATCH_REQUEST, id));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        Assert.assertEquals(response.getStatusCode(), 200);
        logger.debug("Response code is : " + response.getBody().asPrettyString());
    }

    public void delayedResponse(int delay)  {
        Response response = null;
        response = RestAssured.given()
                .baseUri(BASE_PATH)
                .accept(ContentType.JSON)
                .get(String.format(DELAY_REQUEST, delay));
        Assert.assertEquals(response.getStatusCode(), 200);
        logger.debug("Response code is : " + response.getBody().asPrettyString());
    }

    public void createUserWithEmailAndPassword(String email, String password) {
        String payload = String.format("{\"email\": \"%s\" , \"password\": \"%s\" }", email, password);
        logger.info("Payload is : "+payload);
        Response response = RestAssured.given()
                .baseUri(BASE_PATH)
                .accept(ContentType.JSON)
                .body(payload)
                .post(REGISTER_REQUEST);
        logger.debug("Response body is : " + response.asPrettyString());
        Assert.assertEquals(200, response.getStatusCode());
    }

    public void createUserWithEmail(String email) {
        String payload = String.format("{\"email\": \"%s\" }", email);
        logger.info("Payload is : "+payload);
        Response response = RestAssured.given()
                .baseUri(BASE_PATH)
                .accept(ContentType.JSON)
                .body(payload)
                .accept(ContentType.JSON)
                .post(REGISTER_REQUEST);
        Assert.assertEquals(response.getStatusCode(), 400);
        logger.debug("Response body is : " + response.asPrettyString());
    }

    public void loginUserWithEmailAndPassword(String email, String password) {
        String payload = String.format("{\"email\": \"%s\" , \"password\": \"%s\" }", email, password);
        logger.info("Payload is : "+payload);
        Response response = RestAssured.given()
                .baseUri(BASE_PATH)
                .accept(ContentType.JSON)
                .body(payload)
                .basePath(LOGIN_REQUEST)
                .accept(ContentType.JSON)
                .post();
        logger.debug("Response body is : " + response.asPrettyString());
        Assert.assertEquals(200, response.getStatusCode());
    }

    public void loginUserWithEmail(String email) {
        String payload = String.format("{\"email\": \"%s\" }", email);
        logger.info("Payload is : "+payload);
        Response response = RestAssured.given()
                .baseUri(BASE_PATH)
                .accept(ContentType.JSON)
                .body(payload)
                .post(LOGIN_REQUEST);
        logger.debug("Response body is : " + response.asPrettyString());
        Assert.assertEquals(400, response.getStatusCode());
    }


}
