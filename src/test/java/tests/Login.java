package tests;

import core.BaseTest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class Login extends BaseTest {

    @Test
    public void Login() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "fulano@qa.com");
        login.put("password", "teste");

         given()
                .body(login)
                .pathParams("entidade", "login")
                .when()
                .post("/{entidade}")
                .then()
                .statusCode(200);
    }
}
