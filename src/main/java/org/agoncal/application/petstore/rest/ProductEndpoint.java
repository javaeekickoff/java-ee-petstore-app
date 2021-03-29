package org.agoncal.application.petstore.rest;

import static jakarta.ws.rs.core.Response.created;
import static jakarta.ws.rs.core.Response.noContent;
import static jakarta.ws.rs.core.Response.ok;
import static jakarta.ws.rs.core.Response.status;
import static jakarta.ws.rs.core.Response.Status.CONFLICT;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.UriBuilder.fromResource;

import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import org.agoncal.application.petstore.model.Product;
import org.agoncal.application.petstore.util.Loggable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@Stateless
@Path("/products")
@Loggable
@Api("Product")
public class ProductEndpoint {

    // ======================================
    // = Attributes =
    // ======================================

    @PersistenceContext(unitName = "applicationPetstorePU")
    private EntityManager entityManager;

    // ======================================
    // = Business methods =
    // ======================================

    @POST
    @Consumes({ "application/xml", "application/json" })
    @ApiOperation("Creates new product")
    public Response create(Product entity) {
        entityManager.persist(entity);
        return created(fromResource(ProductEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @ApiOperation("Deletes a product by id")
    public Response deleteById(@PathParam("id") Long id) {
        Product entity = entityManager.find(Product.class, id);
        if (entity == null) {
            return status(NOT_FOUND).build();
        }
        entityManager.remove(entity);
        return noContent().build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces({ "application/xml", "application/json" })
    @ApiOperation("Finds a product by id")
    public Response findById(@PathParam("id") Long id) {
        TypedQuery<Product> findByIdQuery = entityManager.createQuery("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.category WHERE p.id = :entityId ORDER BY p.id",
                Product.class);
        findByIdQuery.setParameter("entityId", id);
        Product entity;
        try {
            entity = findByIdQuery.getSingleResult();
        } catch (NoResultException nre) {
            entity = null;
        }
        if (entity == null) {
            return status(NOT_FOUND).build();
        }

        return ok(entity).build();
    }

    @GET
    @Produces({ "application/xml", "application/json" })
    @ApiOperation("Lists all products")
    public List<Product> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
        TypedQuery<Product> findAllQuery = entityManager.createQuery("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.category ORDER BY p.id", Product.class);
        if (startPosition != null) {
            findAllQuery.setFirstResult(startPosition);
        }
        if (maxResult != null) {
            findAllQuery.setMaxResults(maxResult);
        }

        return findAllQuery.getResultList();
    }

    @PUT
    @Path("/{id:[0-9][0-9]*}")
    @Consumes({ "application/xml", "application/json" })
    @ApiOperation("Updates a product")
    public Response update(Product entity) {
        try {
            entity = entityManager.merge(entity);
        } catch (OptimisticLockException e) {
            return status(CONFLICT).entity(e.getEntity()).build();
        }

        return noContent().build();
    }
}
