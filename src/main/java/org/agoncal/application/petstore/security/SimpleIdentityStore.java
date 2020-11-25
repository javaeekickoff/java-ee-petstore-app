package org.agoncal.application.petstore.security;

import static javax.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

import org.agoncal.application.petstore.model.Customer;
import org.agoncal.application.petstore.service.CustomerService;

@CustomFormAuthenticationMechanismDefinition(loginToContinue = @LoginToContinue())
@ApplicationScoped
public class SimpleIdentityStore implements IdentityStore {

    @Inject
    private CustomerService customerService;

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {

        Customer customer = customerService.findCustomer(credential.getCaller(), credential.getPasswordAsString());
        if (customer == null) {
            return INVALID_RESULT;
        }

        return new CredentialValidationResult(new SimpleCallerPrincipal(customer));
    }

}
