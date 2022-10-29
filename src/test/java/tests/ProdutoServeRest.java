package tests;

import core.BaseRequest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runners.MethodSorters;
import utils.GeradorDeHash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProdutoServeRest extends BaseRequest {

    public static final String ENDPOINT_PRODUTOS = "/produtos";

    @Test
    @DisplayName("Lista de Produtos")
    public void CT01_validarListaDeProdutos() {
        ArrayList<String> nome = given()

                .when()
                .get(ENDPOINT_PRODUTOS)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().path("produtos.nome.findAll{it.contains('Jaws')}");
                //.extract().path("produtos.nome.findAll{it.contains('Jaws')}");

    }

    @Test
    @DisplayName("Produto Cadastrado")
    public void CT02_validarCadastroDeProdutos() {

        Map<String, Object> params = new HashMap<>();
        params.put("nome", GeradorDeHash.getGerarStringAleatoria(6));
        params.put("preco", 12000);
        params.put("descricao", "celular");
        params.put("quantidade", 5);

        given()
                .body(params)
                .when()
                .post(ENDPOINT_PRODUTOS)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", is("Cadastro realizado com sucesso"))
                .body("$", hasKey("_id"));


    }

    @Test
    @DisplayName("Produto Já existente")
    public void CT03_validarCadastroDeProdutosJaExistente() {

        Map<String, Object> params = new HashMap<>();
        params.put("nome", "Jaws");
        params.put("preco", 45);
        params.put("descricao", "celular");
        params.put("quantidade", 5);

        given()
                .body(params)
                .when()
                .post(ENDPOINT_PRODUTOS)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Já existe produto com esse nome"))
        ;


    }

    @Test
    @DisplayName("Busca de produto por ID")
    public void CT04_validarBuscaDeProdutoPorId() {

        Response response = given()
                .pathParams("_id", "BeeJh5lz3k6kSIzA")
                .when()
                .get(ENDPOINT_PRODUTOS + "/{_id}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", is(5))
                .extract().response();

        JsonPath jsonPath = new JsonPath(response.body().asString());
        assertTrue(jsonPath.getString("$").contains("nome"));
        assertTrue(jsonPath.getString("$").contains("preco"));
        assertTrue(jsonPath.getString("$").contains("descricao"));
        assertTrue(jsonPath.getString("$").contains("quantidade"));
        assertTrue(jsonPath.getString("$").contains("_id"));

    }

    @Test
    @DisplayName("Produto não encontrado")
    public void CT05_validarProdutoNaoIdentificado() {

        given()
                .pathParams("_id", GeradorDeHash.getGerarStringAleatoria(10))
                .when()
                .get(ENDPOINT_PRODUTOS + "/{_id}")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Produto não encontrado"));

    }
    @Test
    @DisplayName("Proibido excluir produto que faz parte do carrinho")
    public void CT06_ExcluirProdutoDoCarrinho() {

        given()
                .pathParams("_id", "BeeJh5lz3k6kSIzA")
                .when()
                .delete(ENDPOINT_PRODUTOS + "/{_id}")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Não é permitido excluir produto que faz parte de carrinho"))
                .body("idCarrinhos",hasItems("qbMqntef4iTOwWfg","zs44COnXjB9ehOZj"));

    }
}
