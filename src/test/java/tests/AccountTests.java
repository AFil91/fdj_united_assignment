package tests;

import config.Config;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;

import static org.hamcrest.Matchers.*;

public class AccountTests {

    @BeforeClass
    public void setup() {
        baseURI = Config.BASE_URL;
    }

    @DataProvider(name = "invalidNames")
    public Object[][] invalidNames() {
        return new Object[][]{
                {"   "},
                {"123"},
                {"@#$%"},
                {"Ana Filote account with very very very very very very very very very very very loooooooong name limit is the sky 2"}
        };
    }

    @Test(groups = {"smoke"})
    public void adminCreateAccountTest() {

        given()
                .auth()
                .preemptive()
                .basic(Config.USERNAME_ADMIN, Config.PASSWORD_ADMIN)
                .contentType(JSON)
                .body("""
                            {
                                "ownerName": "John Auto",
                                "ownerUsername": "user"
                            }
                        """)
                .when()
                .post("/accounts")
                .then()
                .log().all()
                .statusCode(201);
    }

    @Test(groups = {"lowPrio"})
    public void adminCreateAccountShouldReturnNonNullDateTest() {

        Response response =
                given()
                    .auth()
                    .preemptive()
                    .basic(Config.USERNAME_ADMIN, Config.PASSWORD_ADMIN)
                    .contentType(JSON)
                .body("""
                            {
                                "ownerName": "John Auto",
                                "ownerUsername": "user"
                            }
                        """)
                .when()
                    .post("/accounts");

        response.then()
                .log().all()
                .statusCode(201)
                .body("createdAt", notNullValue());
    }

    @Test(groups = {"smoke"})
    public void userCreateAccountTest() {

        given()
                .auth()
                .preemptive()
                .basic(Config.USERNAME_USER, Config.PASSWORD_USER)
                .contentType(JSON)
                .body("""
                            {
                                "ownerName": "John Auto",
                                "ownerUsername": "user"
                            }
                        """)
                .when()
                .post("/accounts")
                .then()
                .log().all()
                .statusCode(403);
    }

    @Test(groups = {"regression"}, dataProvider = "invalidNames")
    public void adminCreateAccountInvalidNamesTest(String name) {

        Response response =
                given()
                    .auth()
                    .preemptive()
                    .basic(Config.USERNAME_ADMIN, Config.PASSWORD_ADMIN)
                    .contentType(JSON)
                    .body(Map.of(
                        "ownerName", name,
                        "ownerUsername", "user"
                     ))
                .when()
                    .post("/accounts");

        response.then()
                .log().all()
                .statusCode(400);
    }
}
