package org.agoncal.application.petstore.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.agoncal.application.petstore.model.Country;
import org.agoncal.application.petstore.util.Loggable;

@Stateless
@LocalBean
@Loggable
public class CountryService extends AbstractService<Country> implements Serializable {

    // ======================================
    // = Constructors =
    // ======================================

    private static final long serialVersionUID = 1L;

    public CountryService() {
        super(Country.class);
    }

    // ======================================
    // = Protected methods =
    // ======================================

    @Override
    protected Predicate[] getSearchPredicates(Root<Country> root, Country example) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<>();

        String isoCode = example.getIsoCode();
        if (isoCode != null && !"".equals(isoCode)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("isoCode")), '%' + isoCode.toLowerCase() + '%'));
        }
        String name = example.getName();
        if (name != null && !"".equals(name)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
        }
        String printableName = example.getPrintableName();
        if (printableName != null && !"".equals(printableName)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("printableName")), '%' + printableName.toLowerCase() + '%'));
        }
        String iso3 = example.getIso3();
        if (iso3 != null && !"".equals(iso3)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("iso3")), '%' + iso3.toLowerCase() + '%'));
        }
        String numcode = example.getNumcode();
        if (numcode != null && !"".equals(numcode)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("numcode")), '%' + numcode.toLowerCase() + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }
}