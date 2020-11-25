package org.agoncal.application.petstore.view;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org -- This interceptor catches exception and displayes them in
 * a JSF page
 */

@Interceptor
@CatchException
public class ExceptionInterceptor implements Serializable {

    /**
     *
     */
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
