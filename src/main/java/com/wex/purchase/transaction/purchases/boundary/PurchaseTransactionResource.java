package com.wex.purchase.transaction.purchases.boundary;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wex.purchase.transaction.purchases.control.PurchaseTransactionService;
import com.wex.purchase.transaction.purchases.control.dto.PurchaseTransactionPayload;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.time.LocalDate;


@Path("/purchase-transaction")
public class PurchaseTransactionResource {

    @Inject
    @Channel("create-purchase-transaction-out")
    Emitter<String> createPurchaseTransactionEmitter;

    @Inject
    PurchaseTransactionService purchaseTransactionService;

    @Inject
    ObjectMapper objectMapper;


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createPurchaseTransaction(@Valid PurchaseTransactionPayload purchasePayload) throws JsonProcessingException {
        validate(purchasePayload.getTransactionDate());

        return Uni.createFrom().completionStage(createPurchaseTransactionEmitter.send(objectMapper.writeValueAsString(purchasePayload)))
                .map(i -> Response.accepted().build());

    }

    @GET
    @Blocking
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getExchangeRateByCountryAndDate(
            @QueryParam("transactionId") @Valid @NotBlank String transactionId,
            @QueryParam("country") @Valid @NotBlank String country,
            @QueryParam("transactionDate") @Valid @NotBlank String transactionDate) throws Exception {

        validate(transactionDate);

        return Response.ok(purchaseTransactionService.getExchangeRateByCountryAndDate(transactionId, country, transactionDate)).build();


    }

    private void validate(String date) {
        try {
            LocalDate.parse(date);
        } catch (Exception e) {
            throw new WebApplicationException(Response.status(400).entity(date + " is Invalid Date format").build());
        }
    }


}
