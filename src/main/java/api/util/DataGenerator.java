package api.util;

import api.model.User;

import java.util.UUID;

public class DataGenerator {

    public static String randomEmail() {
        return "polina_" + UUID.randomUUID().toString().substring(0, 8) + "@yandex.ru";
    }

    public static String randomPassword() {
        return "pass_" + UUID.randomUUID().toString().substring(0, 6);
    }

    public static String randomName() {
        return "Polina_" + UUID.randomUUID().toString().substring(0, 4);
    }

    public static User randomUser() {
        return new User(randomEmail(), randomPassword(), randomName());
    }
}