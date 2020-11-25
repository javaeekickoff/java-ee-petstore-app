package org.agoncal.application.petstore.util;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org -- This interceptor implements Serializable because it's
 * used on a Stateful Session Bean who has passivation and activation lifecycle.
 */

@Loggable
@Interceptor
public class LoggingInterceptor implements Serializable {

    // ======================================
    // = Attributes =
    // ======================================

    private static final long serialVersionUID = 1L;

    @Inject
    private transient Logger logger;

    // ======================================
    // = Business methods =
    // ======================================

    @AroundInvoke
    private Object intercept(InvocationContext ic) throws Exception {
        logger.entering(ic.getTarget().getClass().getName(), ic.getMethod().getName());
        logger.info(">>> " + ic.getTarget().getClass().getName() + "-" + ic.getMethod().getName());
        try {
            return ic.proceed();
        } finally {
            logger.exiting(ic.getTarget().getClass().getName(), ic.getMethod().getName());
            logger.info("<<< " + ic.getTarget().getClass().getName() + "-" + ic.getMethod().getName());
        }
    }
}
