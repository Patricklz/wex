package com.wex.purchase.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wex.purchase.transaction.purchases.boundary.PurchaseTransactionResource;
import com.wex.purchase.transaction.purchases.control.PurchaseTransactionService;
import com.wex.purchase.transaction.purchases.control.dto.PurchaseTransactionPayload;
import com.wex.purchase.transaction.purchases.model.PurchaseTransaction;
import com.wex.purchase.transaction.purchases.model.PurchaseTransactionRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@TestHTTPEndpoint(PurchaseTransactionResource.class)
@QuarkusTest
public class PurchaseTransactionResourceTest {

    @Test
    public void testPurchaseTransactionEndpoint(){

        PurchaseTransactionPayload purchaseTransactionPayload = new PurchaseTransactionPayload();
        purchaseTransactionPayload.setDescription("test-description");
        purchaseTransactionPayload.setTransaction(UUID.randomUUID().toString());
        purchaseTransactionPayload.setTransactionDate("2023-10-01");
        purchaseTransactionPayload.setAmount(new BigDecimal(100));

        given()
                .body(purchaseTransactionPayload)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
          .when().post()
          .then()
             .statusCode(202);
    }

    @Test
    public void testErrorDescriptionEndpoint(){

        PurchaseTransactionPayload purchaseTransactionPayload = new PurchaseTransactionPayload();
        purchaseTransactionPayload.setDescription("test-description-test-description-test-description-test-description-test-description-test-description-test-description-test-description");
        purchaseTransactionPayload.setTransaction(UUID.randomUUID().toString());
        purchaseTransactionPayload.setTransactionDate("2023-10-01");
        purchaseTransactionPayload.setAmount(new BigDecimal(100));


        given()
                .body(purchaseTransactionPayload)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .when().post()
                .then()
                .statusCode(400)
                .body(is("{\"title\":\"Constraint Violation\",\"status\":400,\"violations\":[{\"field\":\"createPurchaseTransaction.purchasePayload.description\",\"message\":\"Size grater then 50 characters\"}]}"));

    }

    @Test
    public void testErrorInvalidDateEndpoint(){
        given()
                .queryParam("transactionId", UUID.randomUUID().toString())
                .queryParam("country", "Brazil")
                .queryParam("transactionDate", "2023-14-14")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .when().get()
                .then()
                .statusCode(400);
    }

    @Test
    public void testErrorPurchaseTransactionNotFoundEndpoint(){
        given()
                .queryParam("transactionId", UUID.randomUUID().toString())
                .queryParam("country", "Brazil")
                .queryParam("transactionDate", "2023-10-01")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .when().get()
                .then()
                .statusCode(404);
    }


    @Test
    public void testErrorPurchaseTransactionBlankEndpoint(){
        given()
                .queryParam("transactionId", "")
                .queryParam("country", "Brazil")
                .queryParam("transactionDate", "2023-10-01")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .when().get()
                .then()
                .statusCode(400);
    }



}