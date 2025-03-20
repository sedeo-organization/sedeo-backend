package com.sedeo.bdd.glue.user;

import com.sedeo.authentication.controller.dto.LoginRequest;
import com.sedeo.authentication.controller.dto.LoginResponse;
import com.sedeo.authentication.controller.dto.RegisterRequest;
import com.sedeo.bdd.context.UserContext;
import com.sedeo.bdd.glue.ResponseAwareSteps;
import com.sedeo.controllers.dto.FetchUserProfileResponse;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import org.junit.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.sedeo.bdd.endpoints.Endpoint.*;
import static io.restassured.RestAssured.given;

public class UserRegistersSteps extends ResponseAwareSteps {

    private final UserContext userContext;

    public UserRegistersSteps(UserContext userContext) {
        this.userContext = userContext;
    }

    @Given("users email {string}, first name {string}, last name {string}")
    public void usersEmailMichalDabkowskiGmailComFirstNameMichalLastNameDabkowski(String email, String firstName, String lastName) {
        userContext.email = email;
        userContext.firstName = firstName;
        userContext.lastName = lastName;
    }

    @And("phone number {string} and password {string}")
    public void phoneNumberAndPasswordStrongPassword(String phoneNumber, String password) {
        userContext.phoneNumber = phoneNumber;
        userContext.password = password;
    }

    @When("user registers")
    public void userRegisters() {
        this.lastResponse = given()
                .contentType(ContentType.JSON)
                .body(new RegisterRequest(userContext.email, userContext.firstName, userContext.lastName, userContext.phoneNumber, userContext.password))
                .post(REGISTRATION);
    }

    @Then("user should be created successfully")
    public void userShouldBeCreatedSuccessfully() {
        Assert.assertEquals(201, this.lastResponse.getStatusCode());
    }

    @When("user tries to log in with email {string} and password {string}")
    public void userTriesToLogInWithEmailMichalDabkowskiGmailComAndPasswordStrongPassword(String email, String password) {
        this.lastResponse = given()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(userContext.email, userContext.password))
                .post(LOGIN);
    }

    @Then("user should be logged in successfully")
    public void userShouldBeLoggedInSuccessfully() {
        Assert.assertEquals(201, this.lastResponse.getStatusCode());
        LoginResponse loginResponse = this.lastResponse.body().as(LoginResponse.class);
        Assert.assertNotNull(loginResponse.jwt());
        userContext.jwt = loginResponse.jwt();
    }

    @When("user tries to view their profile")
    public void userTriesToViewTheirProfile() {
        this.lastResponse = given()
                .contentType(ContentType.JSON)
                .header("Authorization", userContext.bearer())
                .get(PROFILE);
    }

    @Then("user should see their email, first name, last name and phone number")
    public void userShouldSeeTheirEmailFirstNameLastNameAndPhoneNumber() {
        FetchUserProfileResponse fetchUserProfileResponse = this.lastResponse.as(FetchUserProfileResponse.class);
        Assert.assertNotNull(fetchUserProfileResponse.userId());
        userContext.userId = fetchUserProfileResponse.userId();
        Assert.assertEquals(fetchUserProfileResponse.email(), userContext.email);
        Assert.assertEquals(fetchUserProfileResponse.firstName(), userContext.firstName);
        Assert.assertEquals(fetchUserProfileResponse.lastName(), userContext.lastName);
        Assert.assertEquals(fetchUserProfileResponse.accountBalance(), BigDecimal.ZERO.setScale(2, RoundingMode.UNNECESSARY));
    }
}
