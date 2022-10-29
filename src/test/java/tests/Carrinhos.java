package tests;

import core.BaseRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Carrinhos extends BaseRequest {

    public static final String ENDPOINT_CARRINHO = "/carrinhos";

    @Test
    @DisplayName("Listar Carrinhos Cadastrados")
    public void CT01_validarListaDeCarrinhos() {
        Response response = given()
                .when()
                .get(ENDPOINT_CARRINHO)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("carrinhos[0].produtos[0]", hasKey("idProduto"))
                .body("carrinhos[0].produtos[0]", hasKey("quantidade"))
                .body("carrinhos[0].produtos[0]", hasKey("precoUnitario"))
                .body(matchesJsonSchemaInClasspath("ListacarrinhosSchema.json"))
                .extract().response();

        JsonPath jsonPath = new JsonPath(response.body().asString());

        assertNotNull(jsonPath);
        assertTrue(jsonPath.getString("$").contains("quantidade"));
        assertTrue(jsonPath.getString("$").contains("carrinhos"));
        assertTrue(jsonPath.getString("carrinhos").contains("produtos"));
        assertTrue(jsonPath.getString("carrinhos").contains("precoTotal"));
        assertTrue(jsonPath.getString("carrinhos").contains("quantidadeTotal"));
        assertTrue(jsonPath.getString("carrinhos").contains("idUsuario"));
        assertTrue(jsonPath.getString("carrinhos").contains("_id"));

    }
    @Test
    @DisplayName("Encontrar carrinho por ID")
    public void CT02_validarBuscaPorCarrinhoPorId(){
        given()
                .pathParams("_id","qbMqntef4iTOwWfg")
                .when()
                    .get(ENDPOINT_CARRINHO+"/{_id}")
                .then()
                    .statusCode(HttpStatus.SC_OK)
                    .body(matchesJsonSchemaInClasspath("BuscaIdCarrinhoSchema.json"));
    }
    @Test
    @DisplayName("Encontrar carrinho por ID")
    public void CT03_ExcluirCarrinho(){
                given()
                    .pathParams("entidade","concluir-compra")
                .when()
                    .delete(ENDPOINT_CARRINHO+"/{entidade}")
                .then()
                    .statusCode(HttpStatus.SC_OK)
                        .body("message",is("Registro excluído com sucesso"));
    }


}



