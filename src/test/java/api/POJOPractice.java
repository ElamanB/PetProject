package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.Request;
import utilities.CashwiseAuthorization;
import utilities.Config;

public class POJOPractice {

    @Test
    public void createCategory() {
        String url = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/categories";
        String token = CashwiseAuthorization.getToken();

        RequestBody requestBody = new RequestBody();
        requestBody.setCategory_title("transportation");
        requestBody.setCategory_description("uber rides");
        requestBody.setFlag(true);

        Response response = RestAssured.given().auth().oauth2(token).contentType(ContentType.JSON)
                .body(requestBody).post(url);

        int status = response.statusCode();
        Assert.assertEquals(201, status);


    }

    @Test
    public void testCustom() throws JsonProcessingException {
        String url = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/categories";
        String token = CashwiseAuthorization.getToken();

        RequestBody requestBody = new RequestBody();
        requestBody.setCategory_title("fruits");
        requestBody.setCategory_description("whole foods");
        requestBody.setFlag(true);

        Response response = RestAssured.given().auth().oauth2(token).contentType(ContentType.JSON)
                .body(requestBody).post(url);

        int status = response.statusCode();
        Assertions.assertEquals(201, status);

        response.prettyPrint();

        ObjectMapper mapper = new ObjectMapper();

        CustomResponse customResponse = mapper.readValue(response.asString(), CustomResponse.class);

        String expectedTitle = customResponse.getCategory_title();
        String expectedCategoryDescription = customResponse.getCategory_description();

        Assert.assertEquals(expectedTitle, "fruits");
        Assert.assertEquals(expectedCategoryDescription, "whole foods");


    }

    @Test
    public void createGetSeller() throws JsonProcessingException {
        String token = CashwiseAuthorization.getToken();
        String url = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers";

        Faker faker = new Faker();
        RequestBody requestBody = new RequestBody();
        requestBody.setSeller_name("Ilon Mask");
        requestBody.setCompany_name("Tesla");
        requestBody.setPhone_number("7844874588");
        requestBody.setAddress("2250 Devon ave");
        requestBody.setEmail(faker.internet().emailAddress());

        Response response = RestAssured.given().auth().oauth2(token).contentType(ContentType.JSON)
                .body(requestBody).post(url);

        int status = response.statusCode();
        Assert.assertEquals(201, status);

        ObjectMapper mapper = new ObjectMapper();
        CustomResponse customResponse = mapper.readValue(response.asString(), CustomResponse.class);

        int id = customResponse.getSeller_id();

        String url1 = Config.getProperty("cashWiseApiUrl") + "/api/myaccount/sellers/" + id;

        Response response1 = RestAssured.given().auth().oauth2(token).get(url1);

        int status2 = response1.statusCode();
        Assert.assertEquals(200, status2);

        String expectedSellerName = customResponse.getSeller_name();
        Assert.assertEquals(requestBody.getSeller_name(), expectedSellerName);

        String expectedCompanyName = customResponse.getCompany_name();
        Assert.assertEquals(requestBody.getCompany_name(), expectedCompanyName);


    }
}
