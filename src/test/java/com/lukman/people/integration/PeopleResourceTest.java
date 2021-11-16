package com.lukman.people.integration;

import com.lukman.people.PersonResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class PeopleResourceTest {

    private static final String XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @Test
    public void testGetReport() {
        byte[] file = given()
                .queryParam("type", PersonResource.FileType.EXCEL)
                .when()
                .get("/people/export")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .header(HttpHeaders.CONTENT_TYPE, XLSX_MIME_TYPE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=customer-report.xlsx")
                .extract().body().asByteArray();

        Assertions.assertTrue(file.length > 0);
    }

    @Test
    public void testGetReportForUnsupportedFileType() {
        given().queryParam("type", PersonResource.FileType.PDF)
                .when()
                .get("/people/export")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

}
