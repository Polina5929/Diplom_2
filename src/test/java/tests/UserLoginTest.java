package tests;

import api.model.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserLoginTest extends BaseTest {

    @Test
    @DisplayName("Авторизация существующего пользователя")
    @Description("Проверяем, что пользователь может войти с валидными данными")
    public void loginExistingUser() {
        ValidatableResponse registerResponse = userClient.register(user);
        accessToken = registerResponse.extract().path("accessToken");

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
    @DisplayName("Авторизация с неверным логином и паролем")
    @Description("Проверяем, что при неверных данных возвращается ошибка")
    public void loginWithWrongCredentials() {
        userClient.register(user).extract().path("accessToken");
        accessToken = userClient.login(user).extract().path("accessToken");

        User wrongUser = new User("wrong_" + user.getEmail(), "wrong_password", user.getName());

        ValidatableResponse loginResponse = userClient.login(wrongUser);

        loginResponse
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}