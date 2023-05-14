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

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

public class PayPalAPI {

    private  TestHelper testHelper = new TestHelper();
    private ObjectMapper objectMapper = new ObjectMapper();
    private String paypalURI = Util.loadClassPathProperty("application.properties", "paypal.base.api");
    private String username = Util.loadClassPathProperty("application.properties", "paypal.clientid");
    private String password = Util.loadClassPathProperty("application.properties", "paypal.clientsecret");
    private static final String V1_OAUTH2_TOKEN = "/v1/oauth2/token";
    private static final String V1_IDENTITY_USERINFO = "/v1/identity/oauth2/userinfo";
    private static final String V1_OAUTH2_TERMINATE = "/v1/oauth2/token/terminate";
    private static final String V2_CHECKOUT_ORDERS = "/v2/checkout/orders";
    private static final String V2_GET_ORDERS = "v2/checkout/orders/%s";
    private static final String V2_INVOICING_TEMPLATES = "v2/invoicing/templates";
    private static final String V2_INVOICING_TEMPLATES_GET = "v2/invoicing/templates/%s";
    private static final String V2_INVOICING_TEMPLATES_GET_ALL = "v2/invoicing/templates?fields=all&page=1&page_size=10";
    private static final String AUTHORIZE = "authorize";
    private static final String CAPTURE = "capture";
    private static final String SLASH = "/";
    private static final String  CONFIRM_PAYMENT_SOURCE = "confirm-payment-source";
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
        logger.info("Response status code is : "+response.getStatusCode());
//        Assert.assertEquals(201, response.getStatusCode());
        testHelper.setPaypalOrderId(response.getBody().jsonPath().getString("id"));
    }

    public  void iGetTheOrderDetails() {
        Response response = RestAssured.given()
                .baseUri(paypalURI)
                .auth()
                .none()
                .headers(getheaders())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .get(String.format(V2_GET_ORDERS, testHelper.getPaypalOrderId()));

        logger.info("Response body is : "+response.getBody().asPrettyString());
        Assert.assertEquals(200, response.getStatusCode());
        Assert.assertEquals("Validating Order details ", response.getBody().jsonPath().getString("status"), "CREATED");
    }

    public  String patchPayload() throws JsonProcessingException {
        List<PatchRequestItem> patchRequestItemList = new ArrayList<>();

        PatchRequestItem patchRequestItem1 = PatchRequestItem.builder()
                        .op("add")
                        .path("/purchase_units/@reference_id=='default'/shipping/address")
                        .value(Value.builder()
                                .addressLine1("123 Townsend St")
                                .addressLine2("Floor 6")
                                .adminArea2("San Francisco")
                                .adminArea1("US")
                                .postalCode("94107")
                                .countryCode("US")
                                .build())
                        .build();

        PatchRequestItem patchRequestItem2 = PatchRequestItem.builder()
                .op("add")
                .path("/purchase_units/@reference_id=='default'/invoice_id")
                .value(null)
                .build();
        patchRequestItemList.add(patchRequestItem1);
        patchRequestItemList.add(patchRequestItem2);
        return objectMapper.writeValueAsString(patchRequestItemList);
    }

    public Map patchOrderHeaders() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("Authorization", "Bearer " + testHelper.getPaypalToken());
        hashMap.put("PayPal-Request-Id", "A v4 style guid");
        hashMap.put("Content-Type", "application/json");
        return  hashMap;

    }

    public  void iUpdateTheOrderDetails() throws JsonProcessingException {
        String payload = this.patchPayload();
        String patchPayload = payload.replace("\"value\":null", "\"value\": \"03012022-3303-01\"");
        logger.info("Payload is : "+ patchPayload);
        Response response = RestAssured.given()
                .baseUri(paypalURI)
                .auth()
                .none()
                .headers(patchOrderHeaders())
                .contentType(ContentType.JSON)
                .body(patchPayload)
                .accept(ContentType.JSON)
                .patch(String.format(V2_GET_ORDERS, testHelper.getPaypalOrderId()));

        logger.info("Response code is : "+response.getStatusCode());
        Assert.assertEquals(204, response.getStatusCode());
        logger.info("Response is : "+response.getBody().asPrettyString());
    }

    public String confirmPayload() throws JsonProcessingException {
        ConfirmOrder confirmOrder = ConfirmOrder.builder()
                .paymentSource(PaymentSource.builder()
                        .paypal(Paypal.builder()
                                .name(Name.builder()
                                        .givenName("John")
                                        .surname("Doe")
                                        .build())
                                .emailAddress("customer@example.com")
                                .experienceContext(ExperienceContext.builder()
                                        .paymentMethodPreference("IMMEDIATE_PAYMENT_REQUIRED")
                                        .paymentMethodSelected("PAYPAL")
                                        .brandName("EXAMPLE INC")
                                        .locale("en-US")
                                        .landingPage("LOGIN")
                                        .shippingPreference("SET_PROVIDED_ADDRESS")
                                        .userAction("PAY_NOW")
                                        .returnUrl("https://example.com/returnUrl")
                                        .cancelUrl("https://example.com/cancelUrl")
                                        .build())
                                .build())
                        .build())
                .build();
        return objectMapper.writeValueAsString(confirmOrder);
    }


    public  void iConfirmTheOrderDetails() throws JsonProcessingException {
        String payload = this.confirmPayload();
        logger.info("Payload is : "+ payload);
        Response response = RestAssured.given()
                .baseUri(paypalURI)
                .auth()
                .none()
                .headers(getheaders())
                .contentType(ContentType.JSON)
                .body(payload)
                .accept(ContentType.JSON)
                .post(String.format(V2_GET_ORDERS, testHelper.getPaypalOrderId()) + SLASH + CONFIRM_PAYMENT_SOURCE);

        logger.info("Response code is : "+response.getStatusCode());
        logger.info("Response  is : "+response.getBody().asPrettyString());
        Assert.assertEquals(200, response.getStatusCode());
    }

    public Map authorizeAndCaptureHeaders() {
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("Authorization", "Bearer " + testHelper.getPaypalToken());
        hashMap.put("PayPal-Request-Id", "A v4 style guid");
        hashMap.put("Content-Type", "application/json");
        hashMap.put("Prefer", "return=representation");
        return  hashMap;

    }

    public  void iAuthorizeTheOrderDetails() {
        Response response = RestAssured.given()
                .baseUri(paypalURI)
                .auth()
                .none()
                .headers(authorizeAndCaptureHeaders())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .post(String.format(V2_GET_ORDERS, testHelper.getPaypalOrderId()) + SLASH + AUTHORIZE);

        logger.info("Response code is : "+response.getStatusCode());
        logger.info("Response  is : "+response.getBody().asPrettyString());
        Assert.assertEquals(422, response.getStatusCode());
    }

    public  void iCaptureTheOrderDetails() {
        Response response = RestAssured.given()
                .baseUri(paypalURI)
                .auth()
                .none()
                .headers(authorizeAndCaptureHeaders())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .post(String.format(V2_GET_ORDERS, testHelper.getPaypalOrderId()) + SLASH + CAPTURE);

        logger.info("Response code is : "+response.getStatusCode());
        Assert.assertEquals(422, response.getStatusCode());
    }

    public String createTemplatePayload() throws IOException, URISyntaxException {
        String payload = Util.loadPayloadFromClassPathFile("Json/CreateTemplate.json");
        String uniqueName = "template_" + Instant.now().getEpochSecond();
        String payloadAfterReplace = payload.replace("\"name\": \"&UniqueId\",", String.format("\"name\": \"%s\",", uniqueName));
        logger.info("Payload is : "+payloadAfterReplace);
        return payloadAfterReplace;
    }

    public  void iCreateATemplateWithPaypal() throws IOException, URISyntaxException {

        Response response = RestAssured.given()
                .baseUri(paypalURI)
                .auth()
                .none()
                .headers(orderSubmitHeaders())
                .contentType(ContentType.JSON)
                .body(createTemplatePayload())
                .accept(ContentType.JSON)
                .post(V2_INVOICING_TEMPLATES);

        logger.info("Response code is : "+response.getStatusCode());
        Assert.assertEquals(201, response.getStatusCode());
        logger.info("Response is : "+response.asPrettyString());
        testHelper.setTemplateId(response.getBody().jsonPath().getString("id"));
    }

    public  void iGetTemplateWithPaypal() {

        Response response = RestAssured.given()
                .baseUri(paypalURI)
                .auth()
                .none()
                .headers(getheaders())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .get(String.format(V2_INVOICING_TEMPLATES_GET, testHelper.getTemplateId()));

        logger.info("Response code is : "+response.getStatusCode());
        Assert.assertEquals(200, response.getStatusCode());
        logger.info("Response is : "+response.asPrettyString());

    }

    public  void iGetAllTemplatesWithPaypal() {

        Response response = RestAssured.given()
                .baseUri(paypalURI)
                .auth()
                .none()
                .headers(getheaders())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .get(V2_INVOICING_TEMPLATES_GET_ALL);

        logger.info("Response code is : "+response.getStatusCode());
        Assert.assertEquals(200, response.getStatusCode());
        logger.info("Response is : "+response.asPrettyString());

    }

    public  void iUpdateTemplateWithPaypal() {

        Response response = RestAssured.given()
                .baseUri(paypalURI)
                .auth()
                .none()
                .headers(orderSubmitHeaders())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .get(String.format(V2_INVOICING_TEMPLATES_GET, testHelper.getTemplateId()));

        logger.info("Response code is : "+response.getStatusCode());
        Assert.assertEquals(200, response.getStatusCode());
        logger.info("Response is : "+response.asPrettyString());
        testHelper.setTemplateId(response.getBody().jsonPath().getString("id"));
    }

    public  void iDeleteTemplateWithPaypal() {

        Response response = RestAssured.given()
                .baseUri(paypalURI)
                .auth()
                .none()
                .headers(getheaders())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .delete(String.format(V2_INVOICING_TEMPLATES_GET, testHelper.getTemplateId()));

        logger.info("Response code is : "+response.getStatusCode());
        Assert.assertEquals(204, response.getStatusCode());
        logger.info("Response is : "+response.asPrettyString());
    }
}
