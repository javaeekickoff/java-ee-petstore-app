package org.agoncal.application.petstore.service;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;

import org.agoncal.application.petstore.model.Category;
import org.agoncal.application.petstore.model.Item;
import org.agoncal.application.petstore.model.Product;
import org.agoncal.application.petstore.util.Loggable;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@Stateless
@Loggable
public class CatalogService implements Serializable {

    // ======================================
    // = Attributes =
    // ======================================


    private static final long serialVersionUID = 1L;

    @Inject
    private EntityManager entityManager;

    // ======================================
    // = Public Methods =
    // ======================================

    public Category findCategory(@NotNull Long categoryId) {
        return entityManager.find(Category.class, categoryId);
    }

    public Category findCategory(@NotNull String categoryName) {
        TypedQuery<Category> typedQuery = entityManager.createNamedQuery(Category.FIND_BY_NAME, Category.class);
        typedQuery.setParameter("pname", categoryName);
        return typedQuery.getSingleResult();
    }

    public List<Category> findAllCategories() {
        TypedQuery<Category> typedQuery = entityManager.createNamedQuery(Category.FIND_ALL, Category.class);
        return typedQuery.getResultList();
    }

    public Category createCategory(@NotNull Category category) {
        entityManager.persist(category);
        return category;
    }

    public Category updateCategory(@NotNull Category category) {
        return entityManager.merge(category);
    }

    public void removeCategory(@NotNull Category category) {
        entityManager.remove(entityManager.merge(category));
    }

    public void removeCategory(@NotNull Long categoryId) {
        removeCategory(findCategory(categoryId));
    }

    public List<Product> findProducts(@NotNull String categoryName) {
        TypedQuery<Product> typedQuery = entityManager.createNamedQuery(Product.FIND_BY_CATEGORY_NAME, Product.class);
        typedQuery.setParameter("pname", categoryName);
        return typedQuery.getResultList();
    }

    public Product findProduct(@NotNull Long productId) {
        Product product = entityManager.find(Product.class, productId);
        return product;
    }

    public List<Product> findAllProducts() {
        TypedQuery<Product> typedQuery = entityManager.createNamedQuery(Product.FIND_ALL, Product.class);
        return typedQuery.getResultList();
    }

    public Product createProduct(@NotNull Product product) {
        if (product.getCategory() != null && product.getCategory().getId() == null) {
            entityManager.persist(product.getCategory());
        }

        entityManager.persist(product);
        return product;
    }

    public Product updateProduct(@NotNull Product product) {
        return entityManager.merge(product);
    }

    public void removeProduct(@NotNull Product product) {
        entityManager.remove(entityManager.merge(product));
    }

    public void removeProduct(@NotNull Long productId) {
        removeProduct(findProduct(productId));
    }

    public List<Item> findItems(@NotNull Long productId) {
        TypedQuery<Item> typedQuery = entityManager.createNamedQuery(Item.FIND_BY_PRODUCT_ID, Item.class);
        typedQuery.setParameter("productId", productId);
        return typedQuery.getResultList();
    }

    public Item findItem(@NotNull Long itemId) {
        return entityManager.find(Item.class, itemId);
    }

    public List<Item> searchItems(String keyword) {
        if (keyword == null) {
            keyword = "";
        }

        TypedQuery<Item> typedQuery = entityManager.createNamedQuery(Item.SEARCH, Item.class);
        typedQuery.setParameter("keyword", "%" + keyword.toUpperCase() + "%");
        return typedQuery.getResultList();
    }

    public List<Item> findAllItems() {
        TypedQuery<Item> typedQuery = entityManager.createNamedQuery(Item.FIND_ALL, Item.class);
        return typedQuery.getResultList();
    }

    public Item createItem(@NotNull Item item) {
        if (item.getProduct() != null && item.getProduct().getId() == null) {
            entityManager.persist(item.getProduct());
            if (item.getProduct().getCategory() != null && item.getProduct().getCategory().getId() == null) {
                entityManager.persist(item.getProduct().getCategory());
            }
        }

        entityManager.persist(item);
        return item;
    }

    public Item updateItem(@NotNull Item item) {
        return entityManager.merge(item);
    }

    public void removeItem(@NotNull Item item) {
        entityManager.remove(entityManager.merge(item));
    }

    public void removeItem(@NotNull Long itemId) {
        removeItem(findItem(itemId));
    }
}
