package org.agoncal.application.petstore.model;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * @author Antonio Goncalves
 */
public class AddressTest {

    // ======================================
    // = Methods =
    // ======================================

    @Test
    public void shouldCheckEqualsAndHashCode() {

        // Checks equals and hashCode is valid
        EqualsVerifier.forClass(Address.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
}