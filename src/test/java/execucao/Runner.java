package execucao;

import core.BaseRequest;
import core.BearerToken;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import tests.Carrinhos;
import tests.Login;
import tests.ProdutoServeRest;
import tests.UsuariosServeRest;

import static io.restassured.RestAssured.requestSpecification;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        Login.class,
        UsuariosServeRest.class,
        ProdutoServeRest.class,
        Carrinhos.class

})
public class Runner extends BaseRequest {
    public static final String token = BearerToken.extrairToken();

    @BeforeClass
    public static void extrairTokenApartirDoLogin() {

        requestSpecification.header("Authorization", token);


    }

}
