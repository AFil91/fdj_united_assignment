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
import static org.testng.AssertJUnit.assertEquals;

public class TransactionTests {
    @BeforeClass
    public void setup() {
        baseURI = Config.BASE_URL;
    }

    @DataProvider(name = "invalidAmounts")
    public Object[][] invalidAmounts() {
        return new Object[][]{
                {-50},
                {"abc"},
                {0.5}
        };
    }

    @Test
    public void adminCreateDepositTransactionTest() {
        Response before = given()
                .auth().preemptive()
                .basic(Config.USERNAME_ADMIN, Config.PASSWORD_ADMIN)
                .when()
                .get("/accounts/1");

        int beforeCount = before.jsonPath().getList("transactions").size();
        double beforeBalance = before.jsonPath().getDouble("balance");


             given()
                .auth()
                .preemptive()
                .basic(Config.USERNAME_ADMIN, Config.PASSWORD_ADMIN)
                .contentType(JSON)
                .body(Map.of("amount", 10))
                .when()
                .post("/accounts/1/deposit")
                     .then()
                     .log().all()
                .statusCode(200);

        Response after = given()
                .auth().preemptive()
                .basic(Config.USERNAME_ADMIN, Config.PASSWORD_ADMIN)
                .when()
                .get("/accounts/1");

        int afterCount = after.jsonPath().getList("transactions").size();
        double afterBalance = after.jsonPath().getDouble("balance");

        assertEquals(afterCount, beforeCount + 1);
        assertEquals(afterBalance, beforeBalance + 10);
    }

    @Test
    public void userCreateDepositTransactionTest() {
        Response before = given()
                .auth().preemptive()
                .basic(Config.USERNAME_USER, Config.PASSWORD_USER)
                .when()
                .get("/accounts/1");

        int beforeCount = before.jsonPath().getList("transactions").size();
        double beforeBalance = before.jsonPath().getDouble("balance");


        given()
                .auth()
                .preemptive()
                .basic(Config.USERNAME_ADMIN, Config.PASSWORD_ADMIN)
                .contentType(JSON)
                .body(Map.of("amount", 10))
                .when()
                .post("/accounts/1/deposit")
                .then()
                .log().all()
                .statusCode(200);

        Response after = given()
                .auth().preemptive()
                .basic(Config.USERNAME_ADMIN, Config.PASSWORD_ADMIN)
                .when()
                .get("/accounts/1");

        int afterCount = after.jsonPath().getList("transactions").size();
        double afterBalance = after.jsonPath().getDouble("balance");

        assertEquals(afterCount, beforeCount + 1);
        assertEquals(afterBalance, beforeBalance + 10);
    }

    @Test(dataProvider = "invalidAmounts")
    public void adminCreateDepositTransactionWithInvalidAmountTest(Object value) {

        given()
                .auth()
                .preemptive()
                .basic(Config.USERNAME_ADMIN, Config.PASSWORD_ADMIN)
                .contentType(JSON)
                .body(Map.of("amount", value))
                .when()
                .post("/accounts/1/deposit")
                .then()
                .log().all()
                .statusCode(400);
    }
}
