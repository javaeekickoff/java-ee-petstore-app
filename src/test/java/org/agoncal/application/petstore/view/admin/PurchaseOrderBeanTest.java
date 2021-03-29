package org.agoncal.application.petstore.view.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import jakarta.inject.Inject;

import org.agoncal.application.petstore.exceptions.ValidationException;
import org.agoncal.application.petstore.model.Address;
import org.agoncal.application.petstore.model.Category;
import org.agoncal.application.petstore.model.Country;
import org.agoncal.application.petstore.model.CreditCard;
import org.agoncal.application.petstore.model.CreditCardType;
import org.agoncal.application.petstore.model.Customer;
import org.agoncal.application.petstore.model.Item;
import org.agoncal.application.petstore.model.OrderLine;
import org.agoncal.application.petstore.model.Product;
import org.agoncal.application.petstore.model.PurchaseOrder;
import org.agoncal.application.petstore.model.UserRole;
import org.agoncal.application.petstore.view.shopping.ShoppingCartItem;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class PurchaseOrderBeanTest {

    // ======================================
    // = Attributes =
    // ======================================

    @Inject
    private PurchaseOrderBean purchaseorderbean;

    // ======================================
    // = Deployment =
    // ======================================

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClasses(PurchaseOrderBean.class, PurchaseOrder.class, Country.class)
                .addClass(Address.class).addClass(Customer.class).addClass(CreditCard.class).addClass(CreditCardType.class).addClass(OrderLine.class)
                .addClass(Category.class).addClass(Product.class).addClass(Item.class).addClass(ShoppingCartItem.class).addClass(UserRole.class)
                .addClass(ValidationException.class)
                .addAsResource("init_db.sql")
                .addAsManifestResource("META-INF/persistence.xml", "persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    // ======================================
    // = Test Cases =
    // ======================================

    @Test
    public void should_be_deployed() {
        assertNotNull(purchaseorderbean);
    }

    @Test
    @Ignore
    public void should_crud() {
        // Creates an object
        Country country = new Country("DV", "Dummy value", "Dummy value", "DMV", "DMV");
        Address address = new Address("78 Gnu Rd", "Texas", "666", country);
        Customer customer = new Customer("Paul", "Mc Cartney", "pmac", "pmac", "paul@beales.com", address);
        CreditCard creditCard = new CreditCard("1234", CreditCardType.MASTER_CARD, "10/12");
        Set<OrderLine> orderLines = new HashSet<>();
        PurchaseOrder purchaseOrder = new PurchaseOrder(customer, creditCard, address);
        purchaseOrder.setOrderLines(orderLines);
        purchaseOrder.setDiscount(12.5F);

        // Inserts the object into the database
        purchaseorderbean.setPurchaseOrder(purchaseOrder);
        purchaseorderbean.create();
        purchaseorderbean.update();
        purchaseOrder = purchaseorderbean.getPurchaseOrder();
        assertNotNull(purchaseOrder.getId());

        // Finds the object from the database and checks it's the right one
        purchaseOrder = purchaseorderbean.findById(purchaseOrder.getId());
        assertEquals(new Float(12.5F), purchaseOrder.getDiscountRate());

        // Deletes the object from the database and checks it's not there anymore
        purchaseorderbean.setId(purchaseOrder.getId());
        purchaseorderbean.create();
        purchaseorderbean.delete();
        purchaseOrder = purchaseorderbean.findById(purchaseOrder.getId());
        assertNull(purchaseOrder);
    }

    @Test
    public void should_paginate() {
        // Creates an empty example
        PurchaseOrder example = new PurchaseOrder();

        // Paginates through the example
        purchaseorderbean.setExample(example);
        purchaseorderbean.paginate();
        assertTrue(purchaseorderbean.getPageItems().size() == purchaseorderbean.getPageSize()
                || purchaseorderbean.getPageItems().size() == purchaseorderbean.getCount());
    }
}
