package org.agoncal.application.petstore.view;

import static jakarta.faces.application.FacesMessage.SEVERITY_ERROR;

import java.io.Serializable;
import java.util.logging.Logger;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org -- This interceptor catches exception and displayes them in
 * a JSF page
 */

@Interceptor
@CatchException
public class ExceptionInterceptor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    @AroundInvoke
    public Object catchException(InvocationContext invocationContext) throws Exception {
        try {
            return invocationContext.proceed();
        } catch (Exception e) {
            addErrorMessage(e.getMessage());
            log.severe("/!\\ " + invocationContext.getTarget().getClass().getName() + " - " + invocationContext.getMethod().getName() + " - " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // TODO to refactor with Controller methods
    protected void addErrorMessage(String message) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(SEVERITY_ERROR, message, null));
    }
}
