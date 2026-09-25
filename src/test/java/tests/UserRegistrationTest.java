package tests;

import api.model.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserRegistrationTest extends BaseTest {

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверяем, что нового пользователя можно зарегистрировать")
    public void createUniqueUser() {
        ValidatableResponse response = userClient.register(user);

        accessToken = response.extract().path("accessToken");

        response
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    @Description("Проверяем, что повторная регистрация возвращает ошибку")
    public void createDuplicateUser() {
        ValidatableResponse firstResponse = userClient.register(user);
        accessToken = firstResponse.extract().path("accessToken");

        ValidatableResponse duplicateResponse = userClient.register(user);

        duplicateResponse
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Проверяем, что без email регистрация невозможна")
    public void createUserWithoutEmail() {
        ValidatableResponse response = userClient.registerWithoutField(user, "email");

        response
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверяем, что без пароля регистрация невозможна")
    public void createUserWithoutPassword() {
        ValidatableResponse response = userClient.registerWithoutField(user, "password");

        response
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверяем, что без имени регистрация невозможна")
    public void createUserWithoutName() {
        ValidatableResponse response = userClient.registerWithoutField(user, "name");

        response
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }
}