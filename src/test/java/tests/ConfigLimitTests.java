package tests;

import config.Config;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class ConfigLimitTests {
    @Test(groups = {"smoke"})
    public void getConfigLimitTest() {

        baseURI = Config.BASE_URL;

        given()
                .contentType(JSON)
        .when()
                .get("/config/limits")
        .then()
                .statusCode(200);
    }
}
