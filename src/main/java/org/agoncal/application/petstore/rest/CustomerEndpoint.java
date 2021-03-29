package org.agoncal.application.petstore.rest;

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

import org.agoncal.application.petstore.model.Customer;
import org.agoncal.application.petstore.util.Loggable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@Stateless
@Path("/customers")
@Loggable
@Api("Customer")
public class CustomerEndpoint {

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
    @ApiOperation("Creates a customer")
    public Response create(Customer entity) {
        entityManager.persist(entity);
        return Response.created(fromResource(CustomerEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @ApiOperation("Deletes a customer by id")
    public Response deleteById(@PathParam("id") Long id) {
        Customer entity = entityManager.find(Customer.class, id);
        if (entity == null) {
            return status(NOT_FOUND).build();
        }
        entityManager.remove(entity);
        return noContent().build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces({ "application/xml", "application/json" })
    @ApiOperation("Finds a customer by it identifier")
    public Response findById(@PathParam("id") Long id) {
        TypedQuery<Customer> findByIdQuery = entityManager
                .createQuery("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.homeAddress.country WHERE c.id = :entityId ORDER BY c.id", Customer.class);
        findByIdQuery.setParameter("entityId", id);

        Customer entity;
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
    @ApiOperation("Lists all the customers")
    public List<Customer> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
        TypedQuery<Customer> findAllQuery = entityManager.createQuery("SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.homeAddress.country ORDER BY c.id",
                Customer.class);
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
    @ApiOperation("Updates a customer")
    public Response update(Customer entity) {
        try {
            entity = entityManager.merge(entity);
        } catch (OptimisticLockException e) {
            return status(CONFLICT).entity(e.getEntity()).build();
        }

        return noContent().build();
    }
}
