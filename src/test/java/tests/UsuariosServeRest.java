package tests;

import core.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.GeradorNomes;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsuariosServeRest extends BaseTest {

    @Test
    public void CT01_verificarListagemDeUsuarios() {

        Response response = given()
                .pathParams("entidade", "usuarios")
                .when()
                .get("/{entidade}")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body("$", hasKey("quantidade"))
                .body("$", hasKey("usuarios"))
                .body("usuarios[0]", hasKey("nome"))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("listaUsuarioSchema.json"))
                .extract()
                .response();


        JsonPath jsonPath = response.jsonPath();
        String jsonString = jsonPath.getString("usuarios");

        assertNotNull(jsonString);
        assertTrue(jsonString.contains("nome"));
        assertTrue(jsonString.contains("email"));
        assertTrue(jsonString.contains("password"));
        assertTrue(jsonString.contains("administrador"));
        assertTrue(jsonString.contains("_id"));

    }
    @Test
    public void CT02_verificarListagemDeUsuariosPorNome() {

        Response response = given()
                    .pathParams("entidade", "usuarios")
                    .queryParams("nome","Fulano da Silva")
                .when()
                    .get("/{entidade}")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK)
                    .body("$", hasKey("quantidade"))
                    .body("$", hasKey("usuarios"))
                    .body("usuarios[0]", hasKey("nome"))
                    .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("listaUsuarioSchema.json"))
                    .extract().response();


        JsonPath jsonPath = new JsonPath(response.body().asString());

        assertEquals(jsonPath.getInt("quantidade"), 1);
        assertEquals(jsonPath.getInt("usuarios.size()"), 1);


    }

    @Test
    public void CT03_validarCadastroDeUsuario() {

        Map<String, String> cadastro = new HashMap<>();
        cadastro.put("nome", GeradorNomes.getGerarStringAleatoria(8));
        cadastro.put("email",GeradorNomes.getGerarStringAleatoria(8)+"@teste.com.br");
        cadastro.put("password","123456");
        cadastro.put("administrador","true");

                given()
                    .pathParams("entidade", "usuarios")
                    .body(cadastro)
                .when()
                    .post("/{entidade}")
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                ;



    }


}
