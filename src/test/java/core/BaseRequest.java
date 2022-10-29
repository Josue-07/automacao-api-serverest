package core;


import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class BaseRequest implements Costantes {

    public static RequestSpecification reqSpecification;
    public static ResponseSpecification respSpecification;

    @BeforeClass
    public static void setup() {

        baseURI = BASE_URI;
        port = PORTA;
        basePath = BASE_PATH;

        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setContentType(CONTENT_TYPE);
        requestSpecBuilder.setAccept("application/json");
        reqSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder();
        responseSpecBuilder.expectResponseTime(lessThanOrEqualTo(TIMEOUT));
        //responseSpecBuilder.log(LogDetail.ALL);
        respSpecification = responseSpecBuilder.build();

        requestSpecification = reqSpecification;
        responseSpecification = respSpecification;
        //requestSpecification.header("Authorization", BearerToken.extrairToken()); // Comente essa linha para poder executar os testes regressivos no Runner

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();


    }
}
