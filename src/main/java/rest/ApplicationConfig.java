package rest;

import exception.MissingInputException;
import exception.mapper.MissingInputMapper;
import exception.mapper.PersonNotFoundMapper;
import exception.mapper.GenericExceptionMapper;
import rest.cors.CorsRequestFilter;
import rest.cors.CorsResponseFilter;

import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

}
