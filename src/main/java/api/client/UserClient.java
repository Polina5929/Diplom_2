package api.client;

import api.config.ApiConfig;
import api.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient {

    @Step("Регистрация пользователя")
    public ValidatableResponse register(User user) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(ApiConfig.BASE_URL)
                .body(user)
                .post(ApiConfig.REGISTER)
                .then();
    }

    @Step("Регистрация пользователя без поля: {excludedField}")
    public ValidatableResponse registerWithoutField(User user, String excludedField) {
        JsonObject jsonObject = new Gson().toJsonTree(user).getAsJsonObject();
        jsonObject.remove(excludedField);

        return given()
                .contentType(ContentType.JSON)
                .baseUri(ApiConfig.BASE_URL)
                .body(jsonObject.toString())
                .post(ApiConfig.REGISTER)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse login(User user) {
        JsonObject jsonObject = new Gson().toJsonTree(user).getAsJsonObject();
        jsonObject.remove("name");

        return given()
                .contentType(ContentType.JSON)
                .baseUri(ApiConfig.BASE_URL)
                .body(jsonObject.toString())
                .post(ApiConfig.LOGIN)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(ApiConfig.BASE_URL)
                .header("Authorization", accessToken)
                .delete(ApiConfig.USER)
                .then();
    }
}