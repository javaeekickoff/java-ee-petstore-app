package org.agoncal.application.petstore.util;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

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