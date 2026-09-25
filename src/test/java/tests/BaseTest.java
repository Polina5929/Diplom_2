package tests;

import api.client.UserClient;
import api.client.OrderClient;
import api.model.User;
import api.util.DataGenerator;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;

public class BaseTest {

    protected UserClient userClient;
    protected OrderClient orderClient;
    protected User user;
    protected String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru";
        userClient = new UserClient();
        orderClient = new OrderClient();
        user = DataGenerator.randomUser();
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }
}