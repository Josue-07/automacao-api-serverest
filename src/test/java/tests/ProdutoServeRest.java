package tests;

import core.BaseTest;
import org.apache.http.HttpStatus;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import utils.GeradorNomes;

import java.util.HashMap;
import java.util.Map;


import static io.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProdutoServeRest extends BaseTest {

    @Test
    public void CT01_validarListaDeProdutos(){
        given()

                .when()
                    .get("/produtos")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void CT02_validarCadastroDeProdutos(){

        Map<String, Object> params = new HashMap<>();
        params.put("nome", GeradorNomes.getGerarStringAleatoria(6));
        params.put("preco",12000);
        params.put("descricao","celular");
        params.put("quantidade",5);

        given()
                .body(params)
                .when()
                .post("/produtos")
                .then()
                .statusCode(HttpStatus.SC_CREATED);
    }

}
