package org.agoncal.application.petstore.rest;

import static org.junit.Assert.assertEquals;

import java.net.URI;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.agoncal.application.petstore.exceptions.ValidationException;
import org.agoncal.application.petstore.model.Address;
import org.agoncal.application.petstore.model.Country;
import org.agoncal.application.petstore.model.Customer;
import org.agoncal.application.petstore.model.UserRole;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public class CustomerEndpointTest {

    // ======================================
    // = Attributes =
    // ======================================

    @ArquillianResource
    private URI baseURL;

    // ======================================
    // = Deployment =
    // ======================================

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                         .addClass(RestApplication.class)
                         .addClass(CustomerEndpoint.class)
                         .addClass(Customer.class)
                         .addClass(Address.class)
                         .addClass(Country.class)
                         .addClass(UserRole.class)
                         .addClass(ValidationException.class)
                         .addAsResource("init_db.sql")
                         .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                         .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    // ======================================
    // = Test Cases =
    // ======================================

    @Test
    public void should_be_deployed() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(baseURL).path("rest").path("customers");
        assertEquals(Response.Status.OK.getStatusCode(), target.request(MediaType.APPLICATION_XML).get().getStatus());
    }

    @Test
    public void should_produce_json() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(baseURL).path("rest").path("customers");
        assertEquals(Response.Status.OK.getStatusCode(), target.request(MediaType.APPLICATION_JSON).get().getStatus());
    }

    @Test
    public void should_produce_xml() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(baseURL).path("rest").path("customers");
        assertEquals(Response.Status.OK.getStatusCode(), target.request(MediaType.APPLICATION_XML).get().getStatus());
    }
}
