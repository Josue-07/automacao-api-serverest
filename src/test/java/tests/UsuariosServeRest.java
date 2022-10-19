package tests;

import core.BaseTest;
import io.restassured.RestAssured;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsuariosServeRest extends BaseTest {

    @Test
    public void CT01_verificarListagemDeUsuarios(){

        given()
                .pathParams("entidade","usuarios")
                .when()
                    .get("/{entidade}")
                .then()
                .log().all()
                .statusCode(200);
    }


}
