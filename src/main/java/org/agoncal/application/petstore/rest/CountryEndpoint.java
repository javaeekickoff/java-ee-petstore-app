package org.agoncal.application.petstore.rest;

import static javax.ws.rs.core.Response.noContent;
import static javax.ws.rs.core.Response.Status.CONFLICT;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.UriBuilder.fromResource;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

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
