package org.agoncal.application.petstore.model;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

/**
 * @author Antonio Goncalves
 */
public class CategoryTest {

    // ======================================
    // = Methods =
    // ======================================

    @Test
    public void shouldCheckEqualsAndHashCode() {

        // Checks equals and hashCode is valid
        EqualsVerifier.forClass(Category.class).suppress(Warning.NONFINAL_FIELDS).verify();
    }
}