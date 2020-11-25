package org.agoncal.application.petstore.security;

import javax.security.enterprise.CallerPrincipal;

import org.agoncal.application.petstore.model.Customer;

public class SimpleCallerPrincipal extends CallerPrincipal {

    private final Customer customer;

    public SimpleCallerPrincipal(Customer customer) {
        super(customer.getLogin());
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }

}
