package rest;
/*
 * author paepke
 * version 1.0
 */

import com.google.gson.Gson;
import entity.dto.PersonDTO;
import exception.MissingInputException;
import exception.PersonNotFoundException;
import facade.IPersonFacade;
import facade.PersonFacade;
import org.glassfish.jersey.internal.Errors;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.ErrorManager;


@Path("/person")
public class PersonResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV,EMF_Creator.Strategy.CREATE);
    private static final IPersonFacade FACADE = PersonFacade.getPersonFacade(EMF);

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response get() {
        return Response.ok().build();
    }
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") int id) throws PersonNotFoundException {
        PersonDTO person = FACADE.getPerson(id);
        return Response.ok(person).build();
    }
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(FACADE.getAllPersons()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPerson(String json) throws MissingInputException {
        PersonDTO personDTO = new Gson().fromJson(json, PersonDTO.class);
        personDTO = FACADE.addPerson(personDTO.getFirstName(), personDTO.getLastName(),personDTO.getPhone());
        return Response.ok(personDTO).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editPerson(String json) throws PersonNotFoundException, MissingInputException {
        PersonDTO personDTO = new Gson().fromJson(json, PersonDTO.class);
        personDTO = FACADE.editPerson(personDTO);
        return Response.ok(personDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePerson(@PathParam("id") int id) throws PersonNotFoundException {
        return Response.ok(FACADE.deletePerson(id)).build();
    }
}
