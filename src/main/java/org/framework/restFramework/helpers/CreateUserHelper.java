package org.framework.restFramework.helpers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.framework.restFramework.model.CreateUser.CreateUserRequestModel;
import org.framework.restFramework.model.CreateUser.CreateUserResponseModel;
import org.framework.utils.ConfigManager;
import org.framework.utils.ExcelUtils;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.framework.restFramework.constants.ConstantData.CREATE_USER;
import static org.framework.restFramework.constants.ConstantData.EXCEL_PATH;
public class CreateUserHelper
{
    ExcelUtils testDataSheet = new ExcelUtils(EXCEL_PATH
            + ConfigManager.getInstance().getString("excelPathCreateUser"), "TestData");

    public ExcelUtils getTestDataSheet() {
        return testDataSheet;
    }
    public CreateUserHelper() {
        RestAssured.baseURI = ConfigManager.getInstance().getString("baseUrl");
        RestAssured.useRelaxedHTTPSValidation();
    }

    public CreateUserRequestModel setTestData(String testCaseRowName)
    {
        CreateUserRequestModel createUserRequestModel = new CreateUserRequestModel();
        int row = testDataSheet.getRowNum(testCaseRowName);
        createUserRequestModel.setName(testDataSheet.getCellData(row, testDataSheet.getColNumber("name")));
        createUserRequestModel.setJob(testDataSheet.getCellData(row, testDataSheet.getColNumber("job")));
        return createUserRequestModel;
    }

    public Response createUser(CreateUserRequestModel createUserRequestModel) {
        return RestAssured
                .given().log().method().log().uri().log().body()
                .contentType(ContentType.JSON)
                .body(createUserRequestModel)
                .when()
                .post(CREATE_USER)
                .andReturn()
                .then().log().status().log().body()
                .extract().response();
    }

    public String retrieveIdNumber (Response response)
    {
        return response.jsonPath().getString("id");
    }
    public void validTestsHelper(Response response, SoftAssert softAssert, CreateUserRequestModel createUserRequestModel) {
        softAssert.assertEquals(response.getStatusCode(), HttpStatus.SC_CREATED, "status code should be 201");
        //assert on response
        List<String> fields = (Arrays.stream(CreateUserResponseModel.class.getDeclaredFields()).map(Field::getName).toList());
        String responseBody = response.body().asString();
        //assert that all fields exist in response
        for (String field : fields) {
            softAssert.assertTrue(responseBody.contains("\"" + field + "\":"), "Response has missing field : " + field);
        }
        softAssert.assertFalse(responseBody.contains("null"), "There is a null value in the response");
        softAssert.assertEquals(response.jsonPath().getString("name"),createUserRequestModel.getName(),"name should be the same");
        softAssert.assertEquals(response.jsonPath().getString("job"),createUserRequestModel.getJob(),"job should be the same");
    }
}
