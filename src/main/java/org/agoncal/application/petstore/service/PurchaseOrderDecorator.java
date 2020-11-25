package org.agoncal.application.petstore.service;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;

@Decorator
public abstract class PurchaseOrderDecorator implements ComputablePurchaseOrder {

    @Inject
    @Delegate
    private ComputablePurchaseOrder delegate;
}