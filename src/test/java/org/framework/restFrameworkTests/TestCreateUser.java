package org.framework.restFrameworkTests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.framework.restFramework.helpers.CreateUserHelper;
import org.framework.restFramework.model.CreateUser.CreateUserRequestModel;
import org.framework.utils.ConfigManager;
import org.framework.utils.ExcelUtils;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class TestCreateUser
{
    SoftAssert softAssert;
    CreateUserHelper createUserHelper;
    CreateUserRequestModel createUserRequestModel;
    ExcelUtils testDataSheet;
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
        testDataSheet = createUserHelper.getTestDataSheet();
    }

    @BeforeMethod
    public void beforeMethodInitialization() {
        createUserRequestModel = createUserHelper.setTestData("validTestData");
        softAssert = new SoftAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate create user response is 201 and user created successfully when using valid data")
    public void validCreateUser() {
        Response response = createUserHelper.createUser(createUserRequestModel);
        //assert on response
        createUserHelper.validTestsHelper(response, softAssert,createUserRequestModel);
        softAssert.assertAll();
    }
}
