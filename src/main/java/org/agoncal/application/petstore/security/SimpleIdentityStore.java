package org.agoncal.application.petstore.security;

import static jakarta.security.enterprise.identitystore.CredentialValidationResult.INVALID_RESULT;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;

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
