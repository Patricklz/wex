package com.wex.purchase.transaction.purchases.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "purchase")
public class PurchaseTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, name = "id", updatable = false, nullable = false)
    private String id;

    @Column(length = 50, name = "description")
    private String description;

    @Column(name = "transaction_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd,HH:mm:ss")
    LocalDateTime transactionDate;

    @PositiveOrZero
    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;

    public String getId() {
        return id;
    }

//    public void setId(String id) {
//        this.id = id;
//    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
