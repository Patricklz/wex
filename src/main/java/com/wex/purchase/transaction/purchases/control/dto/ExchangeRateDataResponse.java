package com.wex.purchase.transaction.purchases.control.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateDataResponse {

    @JsonProperty("country_currency_desc")
    private String countryCurrencyDesc;
    @JsonProperty("exchange_rate")
    private String exchangeRate;
    @JsonProperty("country")
    private String country;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("record_date")
    private LocalDate recordDate;

    public String getCountryCurrencyDesc() {
        return countryCurrencyDesc;
    }

    public void setCountryCurrencyDesc(String countryCurrencyDesc) {
        this.countryCurrencyDesc = countryCurrencyDesc;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }
}
