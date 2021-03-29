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
import org.agoncal.application.petstore.model.Product;
import org.agoncal.application.petstore.util.Loggable;

@Stateless
@LocalBean
@Loggable
public class ProductService extends AbstractService<Product> implements Serializable {

    // ======================================
    // = Constructors =
    // ======================================

    private static final long serialVersionUID = 1L;

    public ProductService() {
        super(Product.class);
    }

    // ======================================
    // = Protected methods =
    // ======================================

    @Override
    protected Predicate[] getSearchPredicates(Root<Product> root, Product example) {
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
        Category category = example.getCategory();
        if (category != null) {
            predicatesList.add(builder.equal(root.get("category"), category));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }
}