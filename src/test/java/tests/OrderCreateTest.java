package tests;

import api.model.Order;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderCreateTest extends BaseTest {

    @Before
    public void createUserForTests() {
        accessToken = userClient.register(user).extract().path("accessToken");
    }

    private List<String> getTwoIngredients() {
        ValidatableResponse ingredientsResponse = orderClient.getIngredients();
        String first = ingredientsResponse.extract().path("data[0]._id");
        String second = ingredientsResponse.extract().path("data[1]._id");
        List<String> ids = new ArrayList<>();
        ids.add(first);
        ids.add(second);
        return ids;
    }

    @Test
    @DisplayName("Создание заказа с авторизацией и ингредиентами")
    @Description("Проверяем успешное создание заказа авторизованным пользователем")
    public void createOrderWithAuthAndIngredients() {
        Order order = new Order(getTwoIngredients());

        ValidatableResponse response = orderClient.create(accessToken, order);

        response
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Проверяем поведение сервера при создании заказа без токена")
    public void createOrderWithoutAuth() {
        Order order = new Order(getTwoIngredients());

        ValidatableResponse response = orderClient.create(null, order);

        response
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверяем, что без ингредиентов вернётся ошибка")
    public void createOrderWithoutIngredients() {
        Order order = new Order(new ArrayList<>());

        ValidatableResponse response = orderClient.create(accessToken, order);

        response
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверяем, что с неверным хешем возвращается ошибка сервера")
    public void createOrderWithInvalidIngredients() {
        List<String> invalidIds = new ArrayList<>();
        invalidIds.add("invalid_hash_1");
        invalidIds.add("invalid_hash_2");
        Order order = new Order(invalidIds);

        ValidatableResponse response = orderClient.create(accessToken, order);

        response.statusCode(500);
    }
}