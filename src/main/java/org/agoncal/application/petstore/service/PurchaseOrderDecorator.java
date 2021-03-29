package org.agoncal.application.petstore.service;

import jakarta.decorator.Decorator;
import jakarta.decorator.Delegate;
import jakarta.inject.Inject;

@Decorator
public abstract class PurchaseOrderDecorator implements ComputablePurchaseOrder {

    @Inject
    @Delegate
    private ComputablePurchaseOrder delegate;
}