package org.agoncal.application.petstore.view.shopping;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@Named
@SessionScoped
public class CredentialsBean implements Serializable {

    // ======================================
    // = Attributes =
    // ======================================

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String login;
    private String password;
    private String password2;

    // ======================================
    // = Getters & setters =
    // ======================================

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
