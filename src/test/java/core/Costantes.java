package core;

import io.restassured.http.ContentType;

public interface Costantes {

    String BASE_URI = "https://serverest.dev";
    Integer PORTA = 443;
    String BASE_PATH = "";

    ContentType CONTENT_TYPE = ContentType.JSON;
    Long TIMEOUT = 8000L;
}
