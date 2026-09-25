package tests;

import api.model.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserLoginTest extends BaseTest {

    @Before
    public void createUserForTests() {
        accessToken = userClient.register(user).extract().path("accessToken");
    }

    @Test
    @DisplayName("Авторизация существующего пользователя")
    @Description("Проверяем, что пользователь может войти с валидными данными")
    public void loginExistingUser() {
        ValidatableResponse loginResponse = userClient.login(user);

        loginResponse
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    @Description("Проверяем, что при неверном логине возвращается ошибка")
    public void loginWithWrongLogin() {
        User wrongUser = new User("wrong_" + user.getEmail(), user.getPassword(), user.getName());

        ValidatableResponse loginResponse = userClient.login(wrongUser);

        loginResponse
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Проверяем, что при неверном пароле возвращается ошибка")
    public void loginWithWrongPassword() {
        User wrongUser = new User(user.getEmail(), "wrong_password", user.getName());

        ValidatableResponse loginResponse = userClient.login(wrongUser);

        loginResponse
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}