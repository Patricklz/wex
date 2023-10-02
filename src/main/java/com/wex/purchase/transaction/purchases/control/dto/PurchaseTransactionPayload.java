package com.wex.purchase.transaction.purchases.control.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class PurchaseTransactionPayload {
    @Size(max = 50, message = "Size grater then 50 characters")
    String description;

    @Pattern(regexp ="^\\d{4}-\\d{2}-\\d{2}$" , message = "invalid date")
    String transactionDate;
    @PositiveOrZero(message = "The amount must be a positive number or zero")
    BigDecimal amount;
    String transaction;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }
}
