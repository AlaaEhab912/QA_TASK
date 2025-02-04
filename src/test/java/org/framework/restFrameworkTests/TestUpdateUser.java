package org.framework.restFrameworkTests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.framework.restFramework.helpers.CreateUserHelper;
import org.framework.restFramework.helpers.UpdateUserHelper;
import org.framework.restFramework.model.CreateUser.CreateUserRequestModel;
import org.framework.restFramework.model.UpdateUser.UpdateUserRequestModel;
import org.framework.utils.ConfigManager;
import org.framework.utils.ExcelUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class TestUpdateUser
{
    SoftAssert softAssert;
    CreateUserHelper createUserHelper;
    CreateUserRequestModel createUserRequestModel;
    ExcelUtils testDataSheet;
    String idNumber;
    UpdateUserHelper updateUserHelper;
    UpdateUserRequestModel updateUserRequestModel;
    @BeforeSuite
    public void name(ITestContext context) {
        if (context.getCurrentXmlTest().getSuite().getName().equals("Default Suite")) {
            ConfigManager.setRealmId(ConfigManager.getDefaultRealm());
        } else {
            ConfigManager.setRealmId(context.getCurrentXmlTest().getSuite().getName());
        }
    }

    @BeforeClass
    public void beforeClassInitialization() {
        createUserHelper = new CreateUserHelper();
        updateUserHelper = new UpdateUserHelper();
        testDataSheet = updateUserHelper.getTestDataSheet();
        Response response = createUserHelper.createUser(createUserHelper.setTestData("validTestData"));
        idNumber = createUserHelper.retrieveIdNumber(response);
    }

    @BeforeMethod
    public void beforeMethodInitialization() {
        updateUserRequestModel = updateUserHelper.setTestData("validTestData");
        softAssert = new SoftAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate update user response is 200 and user updated successfully when using valid data")
    public void validUpdateUser() {
        Response response = updateUserHelper.updateUser(updateUserRequestModel,idNumber);
        //assert on response
        updateUserHelper.validTestsHelper(response, softAssert,updateUserRequestModel);
        softAssert.assertAll();
    }
}
