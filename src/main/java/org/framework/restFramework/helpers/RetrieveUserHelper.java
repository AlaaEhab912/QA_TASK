package org.framework.restFramework.helpers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.framework.restFramework.model.CreateUser.CreateUserRequestModel;
import org.framework.restFramework.model.RetrieveUser.Response.Data;
import org.framework.restFramework.model.RetrieveUser.Response.RetrieveUserResponseModel;
import org.framework.restFramework.model.RetrieveUser.Response.Support;
import org.framework.utils.ConfigManager;
import org.framework.utils.ExcelUtils;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.framework.restFramework.constants.ConstantData.*;
import static org.testng.Assert.assertEquals;

public class RetrieveUserHelper
{
    ExcelUtils testDataSheet = new ExcelUtils(EXCEL_PATH
            + ConfigManager.getInstance().getString("excelPathRetrieveUser"), "TestData");

    public RetrieveUserHelper() {
        RestAssured.baseURI = ConfigManager.getInstance().getString("baseUrl");
        RestAssured.useRelaxedHTTPSValidation();
    }

    public Response retrieveUser(String idNumber) {
        return RestAssured
                .given().log().method().log().uri().log().body()
                .contentType(ContentType.JSON)
                .pathParam("idNumber", idNumber)
                .when()
                .get(RETRIEVE_USER)
                .andReturn()
                .then().log().status().log().body()
                .extract().response();
    }

    public void validTestsHelper(Response response, SoftAssert softAssert, CreateUserRequestModel createUserRequestModel) {
        assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "status code should be 200");
        //assert on response
        List<String> fields = (Arrays.stream(RetrieveUserResponseModel.class.getDeclaredFields()).map(Field::getName).toList());
        String responseBody = response.body().asString();
        //assert that all fields exist in response
        for (String field : fields) {
            softAssert.assertTrue(responseBody.contains("\"" + field + "\":"), "Response has missing field : " + field);
        }
        fields = Arrays.stream(Support.class.getDeclaredFields()).map(Field::getName).toList();
        for (String field : fields) {
            softAssert.assertTrue(responseBody.contains("\"" + field + "\":")
                    , "support in user details response has missing field : " + field);
        }
        fields = Arrays.stream(Data.class.getDeclaredFields()).map(Field::getName).toList();
        for (String field : fields) {
            softAssert.assertTrue(responseBody.contains("\"" + field + "\":")
                    , "data in user details response has missing field : " + field);
        }
        softAssert.assertFalse(responseBody.contains("null"), "There is a null value in the response");
        softAssert.assertEquals(response.jsonPath().getJsonObject("data.name").toString(),createUserRequestModel.getName(),"name should be the same");
        softAssert.assertEquals(response.jsonPath().getJsonObject("data.job").toString(),createUserRequestModel.getJob(),"job should be the same");
    }
}
