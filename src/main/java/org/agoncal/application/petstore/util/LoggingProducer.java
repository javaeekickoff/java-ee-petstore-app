package org.agoncal.application.petstore.util;

import java.util.logging.Logger;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

public class LoggingProducer {

    // ======================================
    // = Business methods =
    // ======================================

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }
}
