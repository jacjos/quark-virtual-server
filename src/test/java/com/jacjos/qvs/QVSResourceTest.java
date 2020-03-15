package com.jacjos.qvs;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class QVSResourceTest {

    @Test
    public void testVirtualServer() {
        given()
          .when()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .get("/rest/employees")
          .then()
             .statusCode(200)
             .body(containsString("success"));
    }

}