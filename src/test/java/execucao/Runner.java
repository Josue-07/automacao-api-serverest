package execucao;

import core.BaseTest;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.Login;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    Login.class
})
public class Runner extends BaseTest {

    @BeforeClass
    public static void extrairTokenJWTApartirDoLogin(){
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

           requestSpecification.header("Authorization","Bearer " + token);


    }

}