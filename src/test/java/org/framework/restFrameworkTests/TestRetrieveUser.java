package org.framework.restFrameworkTests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.framework.restFramework.helpers.CreateUserHelper;
import org.framework.restFramework.helpers.RetrieveUserHelper;
import org.framework.restFramework.model.CreateUser.CreateUserRequestModel;
import org.framework.utils.ConfigManager;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class TestRetrieveUser
{
    SoftAssert softAssert;
    CreateUserHelper createUserHelper;
    CreateUserRequestModel createUserRequestModel;
    RetrieveUserHelper retrieveUserHelper;
    String idNumber;
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
        retrieveUserHelper = new RetrieveUserHelper();
        Response response = createUserHelper.createUser(createUserHelper.setTestData("validTestData"));
        idNumber = createUserHelper.retrieveIdNumber(response);

    }

    @BeforeMethod
    public void beforeMethodInitialization() {
        softAssert = new SoftAssert();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate retrieve user response is 200 and user data retrieved successfully when using user just created")
    public void validRetrieveNewUser() {
        Response response = retrieveUserHelper.retrieveUser(idNumber);
        //assert on response
        retrieveUserHelper.validTestsHelper(response, softAssert,createUserRequestModel);
        softAssert.assertAll();
    }
}
