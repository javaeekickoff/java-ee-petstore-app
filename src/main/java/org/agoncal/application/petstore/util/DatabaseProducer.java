package org.agoncal.application.petstore.util;

import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

public class DatabaseProducer {

    // ======================================
    // = Attributes =
    // ======================================

    @Produces
    @PersistenceContext(unitName = "applicationPetstorePU")
    private EntityManager entityManager;
}
