package org.agoncal.application.petstore.model;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * @author Antonio Goncalves
 */
public class CustomerTest {

    // ======================================
    // = Methods =
    // ======================================

    @Test
    public void shouldCheckEqualsAndHashCode() {

        // Checks equals and hashCode is valid
        EqualsVerifier.forClass(Customer.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
}