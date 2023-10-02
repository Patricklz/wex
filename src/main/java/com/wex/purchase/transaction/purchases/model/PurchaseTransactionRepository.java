package com.wex.purchase.transaction.purchases.model;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class PurchaseTransactionRepository implements PanacheRepositoryBase<PurchaseTransaction, String> {
}
