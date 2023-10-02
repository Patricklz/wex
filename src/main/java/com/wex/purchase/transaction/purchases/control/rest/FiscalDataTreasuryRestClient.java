package com.wex.purchase.transaction.purchases.control.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


@RegisterRestClient(configKey = "fiscal-data-treasury-api")
public interface FiscalDataTreasuryRestClient {

    @GET
    @Retry
    @Path("/v1/accounting/od/rates_of_exchange")
    Response ratesOfExchange(@QueryParam("fields") String fields, @QueryParam("filter") String filter, @QueryParam("sort") String sort);
}
