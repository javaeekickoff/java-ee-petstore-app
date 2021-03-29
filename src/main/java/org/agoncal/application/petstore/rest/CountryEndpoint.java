package org.agoncal.application.petstore.rest;

import static jakarta.ws.rs.core.Response.noContent;
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

import org.agoncal.application.petstore.model.Country;
import org.agoncal.application.petstore.util.Loggable;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Antonio Goncalves http://www.antoniogoncalves.org --
 */

@Stateless
@Path("/countries")
@Loggable
@Api("Country")
public class CountryEndpoint {

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
    @ApiOperation("Creates a country")
    public Response create(Country entity) {
        entityManager.persist(entity);
        return Response.created(fromResource(CountryEndpoint.class).path(String.valueOf(entity.getId())).build()).build();
    }

    @DELETE
    @Path("/{id:[0-9][0-9]*}")
    @ApiOperation("Deletes a country given an id")
    public Response deleteById(@PathParam("id") Long id) {
        Country entity = entityManager.find(Country.class, id);
        if (entity == null) {
            return Response.status(NOT_FOUND).build();
        }
        entityManager.remove(entity);
        return noContent().build();
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces({ "application/xml", "application/json" })
    @ApiOperation("Retrieves a country by its id")
    public Response findById(@PathParam("id") Long id) {
        TypedQuery<Country> findByIdQuery = entityManager.createQuery("SELECT DISTINCT c FROM Country c WHERE c.id = :entityId ORDER BY c.id", Country.class);
        findByIdQuery.setParameter("entityId", id);
        Country entity;
        try {
            entity = findByIdQuery.getSingleResult();
        } catch (NoResultException nre) {
            entity = null;
        }
        if (entity == null) {
            return Response.status(NOT_FOUND).build();
        }

        return Response.ok(entity).build();
    }

    @GET
    @Produces({ "application/xml", "application/json" })
    @ApiOperation("Lists all the countries")
    public List<Country> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult) {
        TypedQuery<Country> findAllQuery = entityManager.createQuery("SELECT DISTINCT c FROM Country c ORDER BY c.id", Country.class);
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
    @ApiOperation("Updates a country")
    public Response update(Country entity) {
        try {
            entity = entityManager.merge(entity);
        } catch (OptimisticLockException e) {
            return Response.status(CONFLICT).entity(e.getEntity()).build();
        }

        return noContent().build();
    }
}
