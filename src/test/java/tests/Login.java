package tests;

import core.BaseTest;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Login extends BaseTest {

    @Test
    public void CT01_Login() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "fulano@qa.com");
        login.put("password", "teste");

         given()
                    .body(login)
                    .pathParams("entidade", "login")
                 .when()
                    .post("/{entidade}")
                .then()
                    .log().all()
                    .statusCode(200);

    }
    @Test
    public void CT02_validarLoginComEmailIncorreto() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "fula@qa.com");
        login.put("password", "teste");

                given()
                    .body(login)
                    .pathParams("entidade", "login")
                .when()
                    .post("/{entidade}")
                .then()
                    .log().all()
                    .statusCode(401)
                    .body("message", is("Email e/ou senha inválidos"));

    }
    @Test
    public void CT03_validarLoginComSenhaIncorreta() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "fulano@qa.com");
        login.put("password", "tes");

        given()
                .body(login)
                .pathParams("entidade", "login")
                .when()
                .post("/{entidade}")
                .then()
                .log().all()
                .statusCode(401)
                .body("message", is("Email e/ou senha inválidos"));

    }
    @Test
    public void CT04_validarLoginComSenha_E_Email_Incorreta() {
        Map<String, String> login = new HashMap<>();
        login.put("email", "fula@qa.com");
        login.put("password", "tes");

        given()
                .body(login)
                .pathParams("entidade", "login")
                .when()
                .post("/{entidade}")
                .then()
                .log().all()
                .statusCode(401)
                .body("message", is("Email e/ou senha inválidos"));

    }
}
