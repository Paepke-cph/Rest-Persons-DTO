package rest;

import exception.MissingInputException;
import exception.mapper.MissingInputMapper;
import exception.mapper.PersonNotFoundMapper;
import exception.mapper.GenericExceptionMapper;

import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(org.glassfish.jersey.server.wadl.internal.WadlResource.class);
        resources.add(GenericExceptionMapper.class);
        resources.add(MissingInputMapper.class);
        resources.add(PersonNotFoundMapper.class);
        resources.add(PersonResource.class);
    }
    
}
