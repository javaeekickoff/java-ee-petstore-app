package org.agoncal.application.petstore.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.constraints.NotNull;

import org.agoncal.application.petstore.exceptions.ValidationException;
import org.agoncal.application.petstore.model.Address;
import org.agoncal.application.petstore.model.Country;
import org.agoncal.application.petstore.model.CreditCard;
import org.agoncal.application.petstore.model.Customer;
import org.agoncal.application.petstore.model.OrderLine;
import org.agoncal.application.petstore.model.PurchaseOrder;
import org.agoncal.application.petstore.util.Loggable;
import org.agoncal.application.petstore.view.shopping.ShoppingCartItem;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@Stateless
@LocalBean
@Loggable
public class PurchaseOrderService extends AbstractService<PurchaseOrder> implements Serializable {

    // ======================================
    // = Attributes =
    // ======================================

    // ======================================
    // = Public Methods =
    // ======================================

    private static final long serialVersionUID = 1L;

    public PurchaseOrder createOrder(@NotNull Customer customer, @NotNull CreditCard creditCard, final List<ShoppingCartItem> cartItems) {

        // OMake sure the object is valid
        if (cartItems == null || cartItems.size() == 0) {
            throw new ValidationException("Shopping cart is empty"); // TODO exception bean validation
        }

        // Creating the order
        Address deliveryAddress = customer.getHomeAddress();
        Country country = entityManager.find(Country.class, customer.getHomeAddress().getCountry().getId());
        deliveryAddress.setCountry(country);
        PurchaseOrder order = new PurchaseOrder(entityManager.merge(customer), creditCard, deliveryAddress);

        // From the shopping cart we create the order lines
        Set<OrderLine> orderLines = new HashSet<>();

        for (ShoppingCartItem cartItem : cartItems) {
            orderLines.add(new OrderLine(cartItem.getQuantity(), entityManager.merge(cartItem.getItem())));
        }
        order.setOrderLines(orderLines);

        // Persists the object to the database
        entityManager.persist(order);

        return order;
    }

    public PurchaseOrder findOrder(@NotNull Long orderId) {
        return entityManager.find(PurchaseOrder.class, orderId);
    }

    public List<PurchaseOrder> findAllOrders() {
        TypedQuery<PurchaseOrder> typedQuery = entityManager.createNamedQuery(PurchaseOrder.FIND_ALL, PurchaseOrder.class);
        return typedQuery.getResultList();
    }

    public void removeOrder(@NotNull PurchaseOrder order) {
        entityManager.remove(entityManager.merge(order));
    }

    // ======================================
    // = Protected methods =
    // ======================================

    @Override
    protected Predicate[] getSearchPredicates(Root<PurchaseOrder> root, PurchaseOrder example) {
        CriteriaBuilder builder = this.entityManager.getCriteriaBuilder();
        List<Predicate> predicatesList = new ArrayList<>();

        String street1 = example.getCustomer().getHomeAddress().getStreet1();
        if (street1 != null && !"".equals(street1)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("street1")), '%' + street1.toLowerCase() + '%'));
        }

        String street2 = example.getCustomer().getHomeAddress().getStreet2();
        if (street2 != null && !"".equals(street2)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("street2")), '%' + street2.toLowerCase() + '%'));
        }

        String city = example.getCustomer().getHomeAddress().getCity();
        if (city != null && !"".equals(city)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("city")), '%' + city.toLowerCase() + '%'));
        }

        String state = example.getCustomer().getHomeAddress().getState();
        if (state != null && !"".equals(state)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("state")), '%' + state.toLowerCase() + '%'));
        }

        String zipcode = example.getCustomer().getHomeAddress().getZipcode();
        if (zipcode != null && !"".equals(zipcode)) {
            predicatesList.add(builder.like(builder.lower(root.<String>get("zipcode")), '%' + zipcode.toLowerCase() + '%'));
        }

        return predicatesList.toArray(new Predicate[predicatesList.size()]);
    }
}
