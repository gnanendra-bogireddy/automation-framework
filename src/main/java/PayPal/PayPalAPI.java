package PayPal;

import PayPal.model.*;
import TestHelper.TestHelper;
import Util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PayPalAPI {

    private  TestHelper testHelper = new TestHelper();
    private ObjectMapper objectMapper = new ObjectMapper();
    private String paypalURI = Util.loadClassPathProperty("application.properties", "paypal.base.api");
    private String username = Util.loadClassPathProperty("application.properties", "paypal.clientid");
    private String password = Util.loadClassPathProperty("application.properties", "paypal.clientsecret");
    private static  final String V1_OAUTH2_TOKEN = "/v1/oauth2/token";
    private static  final String V1_IDENTITY_USERINFO = "/v1/identity/oauth2/userinfo";
    private static  final String V1_OAUTH2_TERMINATE = "/v1/oauth2/token/terminate";
    private static final String V2_CHECKOUT_ORDERS = "/v2/checkout/orders";
    private static Logger logger = LoggerFactory.getLogger(PayPalAPI.class);

    public Map authTokenPayload() {
        Map<String, String> authParams = new HashMap<>();
        authParams.put("grant_type", "client_credentials");
        authParams.put("ignoreCache", "true");
        authParams.put("return_authn_schemes", "true");
        authParams.put("return_client_metadata", "true");
        authParams.put("return_unconsented_scopes", "true");
        return authParams;
    }

    public void iGetPaypalToken() {

        Response response = RestAssured.given()
                .baseUri(paypalURI)
                .auth()
                .preemptive()
                .basic(username, password)
                .contentType(ContentType.URLENC)
                .formParams(authTokenPayload())
                .accept(ContentType.JSON)
                .post(V1_OAUTH2_TOKEN);

        logger.info("Response code is : "+response.getStatusCode());
        logger.info("Response body is : "+response.getBody().asPrettyString());
        Assert.assertEquals(200, response.getStatusCode());

        testHelper.setPaypalToken(response.getBody().jsonPath().getJsonObject("access_token"));
    }

    public void iGetUserInfo() {
        Response response = RestAssured.given()
                .baseUri(paypalURI)
                .auth()
                .none()
                .headers(getheaders())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .queryParam("schema", "paypalv1.1")
                .get(V1_IDENTITY_USERINFO);


        logger.info("Response code is : "+response.getStatusCode());
        logger.info("Response body is : "+response.getBody().asPrettyString());
        Assert.assertEquals(200, response.getStatusCode());
    }

    public  Map getheaders() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("Authorization", "Bearer " + testHelper.getPaypalToken());
        return  hashMap;
    }

    public  void iTerminateAccessToken() {
        String payload = "token=%s&token_type_hint=ACCESS_TOKEN";
        Response response = RestAssured.given()
                .baseUri(paypalURI)
                .auth()
                .none()
                .headers(getheaders())
                .contentType(ContentType.URLENC)
                .body(String.format(payload, testHelper.getPaypalToken()))
                .accept(ContentType.JSON)
                .post(V1_OAUTH2_TERMINATE);

        logger.info("Response body is : "+response.getBody().asPrettyString());
        Assert.assertEquals(200, response.getStatusCode());
    }

    public String orderPayload() {
        List<Items> itemsList = new ArrayList<>();
        List<PurchaseUnits> purchaseUnits = new ArrayList<>();
        Items items = Items.builder()
                .name("T-Shirt")
                .description("Green XL")
                .quantity("1")
                .unitAmount(UnitAmount.builder()
                        .currencyCode("USD")
                        .value("100.00")
                        .build())
                .build();
        itemsList.add(items);


        PurchaseUnits purchase = PurchaseUnits.builder()
                .itemsList(itemsList)
                .amount(Amount.builder()
                        .currencyCode("USD")
                        .value("100.00")
                        .breakdown(Breakdown.builder()
                                .itemTotal(ItemTotal.builder()
                                        .currencyCode("USD")
                                        .value("100.00")
                                        .build())
                                .build())
                        .build())
                .build();

        purchaseUnits.add(purchase);

        OrderModel payloadBuilder = OrderModel.builder()
                .intent("CAPTURE")
                .purchageUnitsList(purchaseUnits)
                .applicationContext(ApplicationContext.builder()
                        .returnUrl("https://example.com/return")
                        .cancelUrl("https://example.com/cancel")
                        .build())
                .build();
        try {
            logger.info("Payload is : "+objectMapper.writeValueAsString(payloadBuilder));
            return objectMapper.writeValueAsString(payloadBuilder);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public Map orderSubmitHeaders() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("Authorization", "Bearer " + testHelper.getPaypalToken());
        hashMap.put("Prefer", "return=representation");
        hashMap.put("PayPal-Request-Id", "A v4 style guid");
        hashMap.put("Content-Type", "application/json");
        return  hashMap;

    }

    public  void iSubmitAnOrder() {
        String payload = this.orderPayload();
        Response response = RestAssured.given()
                .baseUri(paypalURI)
                .auth()
                .none()
                .headers(orderSubmitHeaders())
                .contentType(ContentType.JSON)
                .body(String.format(payload, testHelper.getPaypalToken()))
                .accept(ContentType.JSON)
                .post(V2_CHECKOUT_ORDERS);

        logger.info("Response body is : "+response.getBody().asPrettyString());
        Assert.assertEquals(201, response.getStatusCode());
    }
}
