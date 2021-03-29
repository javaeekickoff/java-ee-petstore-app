package org.agoncal.application.petstore.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.agoncal.application.petstore.model.Category;
import org.agoncal.application.petstore.util.Loggable;

@Stateless
@LocalBean
@Loggable
public class CategoryService extends AbstractService<Category> implements Serializable {

    // ======================================
    // = Constructors =
    // ======================================

    private static final long serialVersionUID = 1L;

    public CategoryService() {
        super(Category.class);
    }

    // ======================================
    // = Protected methods =
    // ======================================

    @Override
    protected Predicate[] getSearchPredicates(Root<Category> root, Category example) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<>();

        String name = example.getName();
        if (name != null && !"".equals(name)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
        }
        String description = example.getDescription();
        if (description != null && !"".equals(description)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("description")), '%' + description.toLowerCase() + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }
}