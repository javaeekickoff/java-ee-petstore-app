package org.agoncal.application.petstore.util;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

public class NumberProducer {

    @Produces
    @Vat
    @Named
    private Float vatRate;

    @Produces
    @Discount
    @Named
    private Float discountRate;
}