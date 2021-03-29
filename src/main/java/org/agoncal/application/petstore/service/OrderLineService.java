package org.agoncal.application.petstore.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.agoncal.application.petstore.model.Item;
import org.agoncal.application.petstore.model.OrderLine;
import org.agoncal.application.petstore.util.Loggable;

@Stateless
@LocalBean
@Loggable
public class OrderLineService extends AbstractService<OrderLine> implements Serializable {

    // ======================================
    // = Constructors =
    // ======================================

    private static final long serialVersionUID = 1L;

    public OrderLineService() {
        super(OrderLine.class);
    }

    // ======================================
    // = Protected methods =
    // ======================================

    @Override
    protected Predicate[] getSearchPredicates(Root<OrderLine> root, OrderLine example) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<>();

        Integer quantity = example.getQuantity();
        if (quantity != null && quantity.intValue() != 0) {
            predicatesList.add(builder.equal(root.get("quantity"), quantity));
        }
        Item item = example.getItem();
        if (item != null) {
            predicatesList.add(builder.equal(root.get("item"), item));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }
}