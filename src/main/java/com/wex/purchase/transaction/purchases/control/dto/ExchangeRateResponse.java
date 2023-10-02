package com.wex.purchase.transaction.purchases.control.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateResponse {

    @JsonProperty("data")
    private List<ExchangeRateDataResponse> data;

    public List<ExchangeRateDataResponse> getData() {
        return data;
    }

    public void setData(List<ExchangeRateDataResponse> data) {
        this.data = data;
    }
}
