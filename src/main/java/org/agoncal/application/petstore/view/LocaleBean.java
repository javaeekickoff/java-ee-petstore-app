package org.agoncal.application.petstore.view;

import java.io.Serializable;
import java.util.Locale;

import jakarta.enterprise.context.SessionScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import org.agoncal.application.petstore.util.Loggable;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@Named
@SessionScoped
@Loggable
public class LocaleBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Produces
    private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

    // ======================================
    // = Business methods =
    // ======================================

    public Locale getLocale() {
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(String language) {
        locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
    }
}