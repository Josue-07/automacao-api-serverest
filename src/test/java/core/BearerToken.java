package core;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BearerToken extends BaseRequest {

    public static String extrairToken() {
        Map<String, String> login = new HashMap<>();
        login.put("email","fulano@qa.com");
        login.put("password","teste");

        String token = given()
                .body(login)
                .pathParams("entidade","login")
                .when()
                .post("/{entidade}")
                .then()
                .statusCode(200)
                .extract().path("authorization");

        return token;
    }
}
