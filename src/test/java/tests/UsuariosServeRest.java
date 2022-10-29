package tests;

import core.BaseRequest;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.GeradorDeHash;


import java.util.HashMap;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UsuariosServeRest extends BaseRequest {

    @Test
    public void CT01_verificarListagemDeUsuarios() {

        Response response = given()
                .pathParams("entidade", "usuarios")
                .when()
                .get("/{entidade}")
                .then()
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
                .queryParams("nome", "Fulano da Silva")
                .when()
                .get("/{entidade}")
                .then()
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
        cadastro.put("nome", GeradorDeHash.getGerarStringAleatoria(8));
        cadastro.put("email", GeradorDeHash.getGerarStringAleatoria(8) + "@teste.com.br");
        cadastro.put("password", "123456");
        cadastro.put("administrador", "true");

        Response response = given()
                .pathParams("entidade", "usuarios")
                .body(cadastro)
                .when()
                .post("/{entidade}")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", is("Cadastro realizado com sucesso"))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("usuarioCadastradoSchema.json"))
                .extract().response();

        JsonPath jsonPath = response.jsonPath();
        String jsonString = jsonPath.getString("$");

        assertNotNull(jsonString);
        assertTrue(jsonString.contains("_id"));


    }

    @Test
    public void CT04_validarEmailJaExistente() {
        Map<String, String> cadastro = new HashMap<>();
        cadastro.put("nome", GeradorDeHash.getGerarStringAleatoria(8));
        cadastro.put("email", "fulano@qa.com");
        cadastro.put("password", "123456");
        cadastro.put("administrador", "true");

        given()
                .pathParams("entidade", "usuarios")
                .body(cadastro)
                .when()
                .post("/{entidade}")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Este email já está sendo usado"));

    }

    @Test
    public void CT05_validarBuscaDeUsuarioPorId() {

        Response response = given()
                .queryParams("_id", "0uxuPY0cbmQhpEz1")
                .when()
                .get("/usuarios")
                .then()
                .statusCode(HttpStatus.SC_OK).extract().response();

        JsonPath jsonPath = response.jsonPath();
        String jsonString = jsonPath.getString("usuarios");

        assertNotNull(jsonString);
        assertTrue(jsonString.contains("_id"));
        assertTrue(jsonString.contains("0uxuPY0cbmQhpEz1"));

    }

    @Test
    public void CT06_validarBuscaDeUsuarioPorIdNaoEncontrado() {

        Response response = given()
                .pathParams("entidade", "usuarios")
                .pathParams("_id", "fdgfdgfg")
                .when()
                .get("{entidade}/{_id}")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("$", hasKey("message")).extract().response();

        JsonPath jsonPath = new JsonPath(response.body().asString());
        assertNotNull(jsonPath);
        assertTrue(jsonPath.getString("message").contains("Usuário não encontrado"));
        assertEquals("Usuário não encontrado", jsonPath.getString("message"));

    }
//    @Test
//    public void CT07_devoExcluirUsuario() {
//
//        Response response = given()
//                .pathParams("entidade", "usuarios")
//                .pathParams("_id", "0uxuPY0cbmQhpEz1")
//                .when()
//                .delete("{entidade}/{_id}")
//                .then()
//                .statusCode(HttpStatus.SC_OK)
//                .body("$", hasKey("message")).extract().response();
//
//        JsonPath jsonPath = new JsonPath(response.body().asString());
//        assertNotNull(jsonPath);
//        assertEquals("Registro excluído com sucesso", jsonPath.getString("message"));
//
//    }


}
