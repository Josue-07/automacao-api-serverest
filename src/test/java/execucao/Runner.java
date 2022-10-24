package execucao;

import core.BaseTest;
import core.BearerToken;
import io.restassured.specification.RequestSpecification;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.Login;
import tests.ProdutoServeRest;
import tests.UsuariosServeRest;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
       ProdutoServeRest.class
})
public class Runner extends BaseTest {
      public static final String token = BearerToken.extrairToken();
    @BeforeClass
    public static void extrairTokenJWTApartirDoLogin(){

        //requestSpecification.header("Authorization", token);


    }

}
