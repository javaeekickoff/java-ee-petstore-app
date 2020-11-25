package org.agoncal.application.petstore.view.shopping;

import static javax.security.enterprise.AuthenticationStatus.SEND_FAILURE;
import static javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

import java.io.Serializable;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.ExternalContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.auth.login.LoginException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.agoncal.application.petstore.model.Customer;
import org.agoncal.application.petstore.security.SimpleCallerPrincipal;
import org.agoncal.application.petstore.service.CustomerService;
import org.agoncal.application.petstore.util.Loggable;
import org.agoncal.application.petstore.view.AbstractBean;
import org.agoncal.application.petstore.view.CatchException;
import org.agoncal.application.petstore.view.LoggedIn;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */

@Named
@SessionScoped
@Loggable
@CatchException
public class AccountBean extends AbstractBean implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final long serialVersionUID = 1L;

    @Inject
    private CustomerService customerService;

    @Inject
    private CredentialsBean credentials;

    @Inject
    private Conversation conversation;

    @Produces
    @LoggedIn
    private Customer loggedinCustomer;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private ExternalContext externalContext;

    private Customer newCustomer;


    // ======================================
    // =              Public Methods        =
    // ======================================

    public String doLogin() throws LoginException {
        if (credentials.getLogin() == null || "".equals(credentials.getLogin())) {
            addWarningMessage("id_filled");
            return null;
        }

        if (credentials.getPassword() == null || "".equals(credentials.getPassword())) {
            addWarningMessage("pwd_filled");
            return null;
        }

        AuthenticationStatus status = securityContext.authenticate((
            HttpServletRequest) externalContext.getRequest(),
            (HttpServletResponse) externalContext.getResponse(),
            withParams()
                            .credential(new UsernamePasswordCredential(credentials.getLogin(), credentials.getPassword())));

        if (status == SEND_FAILURE) {
            addErrorMessage("FAIL");
            return null;
        }

        return "main.faces";
    }

    public String doCreateNewAccount() {

        // Login has to be unique
        if (customerService.doesLoginAlreadyExist(credentials.getLogin())) {
            addWarningMessage("login_exists");
            return null;
        }

        // Id and password must be filled
        if ("".equals(credentials.getLogin()) || "".equals(credentials.getPassword()) || "".equals(credentials.getPassword2())) {
            addWarningMessage("id_pwd_filled");
            return null;
        }

        if (!credentials.getPassword().equals(credentials.getPassword2())) {
            addWarningMessage("both_pwd_same");
            return null;
        }

        // Login and password are ok
        newCustomer = new Customer();
        newCustomer.setLogin(credentials.getLogin());
        newCustomer.setPassword(credentials.getPassword());

        return "createaccount.faces";
    }

    public String doCreateCustomer() {
        customerService.createCustomer(newCustomer);

        securityContext.authenticate((
                HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse(),
                withParams()
                                .credential(new UsernamePasswordCredential(newCustomer.getLogin(), newCustomer.getPassword())));


        return "main.faces";
    }

    public String doLogout() {
        try {
            ((HttpServletRequest) externalContext.getRequest()).logout();
        } catch (ServletException e) {
            e.printStackTrace();
        }

        loggedinCustomer = null;

        // Stop conversation
        if (!conversation.isTransient()) {
            conversation.end();
        }

        addInformationMessage("been_loggedout");
        return "main.faces";
    }

    public String doUpdateAccount() {
        loggedinCustomer = customerService.updateCustomer(loggedinCustomer);
        addInformationMessage("account_updated");
        return "showaccount.faces";
    }

    public boolean isLoggedIn() {
        return getLoggedinCustomer() != null;
    }

    public Customer getLoggedinCustomer() {
        if (loggedinCustomer == null) {
            loggedinCustomer = securityContext
                .getPrincipalsByType(SimpleCallerPrincipal.class).stream()
                .map(SimpleCallerPrincipal::getCustomer)
                .findAny().orElse(null);
        }

        return loggedinCustomer;
    }

    public Customer getNewCustomer() {
        return newCustomer;
    }

    public void setNewCustomer(Customer newCustomer) {
        this.newCustomer = newCustomer;
    }

}
