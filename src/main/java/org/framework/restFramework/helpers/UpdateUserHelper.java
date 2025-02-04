package org.framework.restFramework.helpers;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.framework.restFramework.model.UpdateUser.UpdateUserRequestModel;
import org.framework.restFramework.model.UpdateUser.UpdateUserResponseModel;
import org.framework.utils.ConfigManager;
import org.framework.utils.ExcelUtils;
import org.testng.asserts.SoftAssert;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.framework.restFramework.constants.ConstantData.*;

public class UpdateUserHelper
{
    ExcelUtils testDataSheet = new ExcelUtils(EXCEL_PATH
            + ConfigManager.getInstance().getString("excelPathUpdateUser"), "TestData");

    public ExcelUtils getTestDataSheet() {
        return testDataSheet;
    }

    public UpdateUserHelper() {
        RestAssured.baseURI = ConfigManager.getInstance().getString("baseUrl");
        RestAssured.useRelaxedHTTPSValidation();
    }

    public UpdateUserRequestModel setTestData(String testCaseRowName)
    {
        UpdateUserRequestModel updateUserRequestModel = new UpdateUserRequestModel();
        int row = testDataSheet.getRowNum(testCaseRowName);
        updateUserRequestModel.setName(testDataSheet.getCellData(row, testDataSheet.getColNumber("name")));
        updateUserRequestModel.setJob(testDataSheet.getCellData(row, testDataSheet.getColNumber("job")));
        return updateUserRequestModel;
    }

    public Response updateUser(UpdateUserRequestModel updateUserRequestModel,String idNumber) {
        return RestAssured
                .given().log().method().log().uri().log().body()
                .contentType(ContentType.JSON)
                .pathParam("idNumber", idNumber)
                .body(updateUserRequestModel)
                .when()
                .put(UPDATE_USER)
                .andReturn()
                .then().log().status().log().body()
                .extract().response();
    }

    public void validTestsHelper(Response response, SoftAssert softAssert, UpdateUserRequestModel updateUserRequestModel) {
        softAssert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK, "status code should be 200");
        //assert on response
        List<String> fields = (Arrays.stream(UpdateUserResponseModel.class.getDeclaredFields()).map(Field::getName).toList());
        String responseBody = response.body().asString();
        //assert that all fields exist in response
        for (String field : fields) {
            softAssert.assertTrue(responseBody.contains("\"" + field + "\":"), "Response has missing field : " + field);
        }
        softAssert.assertFalse(responseBody.contains("null"), "There is a null value in the response");
        softAssert.assertEquals(response.jsonPath().getString("name"),updateUserRequestModel.getName(),"name should be the same");
        softAssert.assertEquals(response.jsonPath().getString("job"),updateUserRequestModel.getJob(),"job should be the same");

    }
}
