package com.wex.purchase.transaction.purchases.control;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wex.purchase.transaction.purchases.control.dto.ExchangeRateDTO;
import com.wex.purchase.transaction.purchases.control.dto.ExchangeRateDataResponse;
import com.wex.purchase.transaction.purchases.control.dto.ExchangeRateResponse;
import com.wex.purchase.transaction.purchases.control.dto.PurchaseTransactionPayload;
import com.wex.purchase.transaction.purchases.control.rest.FiscalDataTreasuryRestClient;
import com.wex.purchase.transaction.purchases.model.PurchaseTransaction;
import com.wex.purchase.transaction.purchases.model.PurchaseTransactionRepository;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

@ApplicationScoped
public class PurchaseTransactionService {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    PurchaseTransactionRepository purchaseTransactionRepository;

    @Inject
    @RestClient
    FiscalDataTreasuryRestClient fiscalDataTreasuryRestClient;

    @ConfigProperty(name = "com.wex.orderrecognition.exchange.rates.fields")
    String fields;

    @ConfigProperty(name = "com.wex.orderrecognition.exchange.rates.sort")
    String sort;

    @Transactional
    @Incoming("create-purchase-transaction-in")
    public void createPurchaseTransactionService(String purchaseMessage) throws Exception {

        PurchaseTransactionPayload dto = objectMapper.readValue(purchaseMessage, PurchaseTransactionPayload.class);

        LocalDate data = LocalDate.parse(dto.getTransactionDate());
        LocalDateTime toLocalDateTime = data.atTime(LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(), LocalDateTime.now().getSecond());

        PurchaseTransaction purchaseTransaction = new PurchaseTransaction();
        purchaseTransaction.setDescription(dto.getDescription());
        purchaseTransaction.setAmount(dto.getAmount());
        purchaseTransaction.setTransactionDate(toLocalDateTime);

        purchaseTransactionRepository.persist(purchaseTransaction);

    }

    @Blocking
    public ExchangeRateDTO getExchangeRateByCountryAndDate(String transactionId, String country, String transactionDate) throws JsonProcessingException {

        PurchaseTransaction purchaseTransaction = getPurchase(transactionId);
        ExchangeRateDataResponse exchangeRateDataResponse = getRatesOfExchange(purchaseTransaction.getTransactionDate(), country, transactionDate);

        return fillExchangeRateDTO(purchaseTransaction, exchangeRateDataResponse);

    }


    private PurchaseTransaction getPurchase(String transactionId) {
       return purchaseTransactionRepository.findByIdOptional(transactionId).orElseThrow(
                () -> {throw new WebApplicationException(Response.status(404).entity("Purchase transaction not found - transactionId: " + transactionId).build());});
    }

    private ExchangeRateDataResponse getRatesOfExchange(LocalDateTime purchaseDate, String country, String transactionDate) throws JsonProcessingException {

        String lowercase = country.toLowerCase();
        country = Pattern.compile("\\b\\w").matcher(lowercase).replaceFirst(m -> m.group().toUpperCase());

        String filter = "country:in:("+country+"),record_date:lte:"+transactionDate;

        Response response = fiscalDataTreasuryRestClient.ratesOfExchange(fields, filter, sort);
        ExchangeRateResponse exchangeRateResponse = objectMapper.readValue(response.readEntity(String.class), ExchangeRateResponse.class);

        if(!exchangeRateResponse.getData().isEmpty()) {
            ExchangeRateDataResponse exchangeRateDataResponse = exchangeRateResponse.getData().get(0);
            LocalDate date = purchaseDate.minusMonths(6).toLocalDate();

            if(exchangeRateDataResponse.getRecordDate().isBefore(date)) {

                throw new WebApplicationException(Response.status(404).entity("No rate were found to apply").build());
            }
        return exchangeRateResponse.getData().get(0);
        } else {
            throw new WebApplicationException(Response.status(404).entity(" Not found").build());
        }
    }

    private ExchangeRateDTO fillExchangeRateDTO(PurchaseTransaction purchaseTransaction, ExchangeRateDataResponse exchangeRateDataResponse) {
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();

        Double exchangeRateRounded = new BigDecimal(exchangeRateDataResponse.getExchangeRate()).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        BigDecimal convertedAmount = purchaseTransaction.getAmount().multiply(new BigDecimal(exchangeRateDataResponse.getExchangeRate())).setScale(2, RoundingMode.HALF_EVEN);

        exchangeRateDTO.setTransactionId(purchaseTransaction.getId());
        exchangeRateDTO.setDescription(exchangeRateDataResponse.getCountryCurrencyDesc());
        exchangeRateDTO.setTransactionDate(exchangeRateDataResponse.getRecordDate());
        exchangeRateDTO.setOriginalAmount(purchaseTransaction.getAmount());
        exchangeRateDTO.setExchangeRate(exchangeRateRounded);
        exchangeRateDTO.setConvertedAmount(convertedAmount);

        return exchangeRateDTO;

    }

}
