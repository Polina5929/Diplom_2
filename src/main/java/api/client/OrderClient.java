package api.client;

import api.config.ApiConfig;
import api.model.Order;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class OrderClient {

    @Step("Создание заказа")
    public ValidatableResponse create(String accessToken, Order order) {
        RequestSpecification request = given()
                .contentType(ContentType.JSON)
                .baseUri(ApiConfig.BASE_URL)
                .body(order);

        if (accessToken != null) {
            request = request.header("Authorization", accessToken);
        }

        return request
                .post(ApiConfig.ORDERS)
                .then();
    }

    @Step("Получение списка ингредиентов")
    public ValidatableResponse getIngredients() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(ApiConfig.BASE_URL)
                .get(ApiConfig.INGREDIENTS)
                .then();
    }
}