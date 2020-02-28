package exception.mapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exception.dto.ExceptionDTO;
import exception.PersonNotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class PersonNotFoundMapper implements ExceptionMapper<PersonNotFoundException> {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public Response toResponse(PersonNotFoundException e) {
        Logger.getLogger(PersonNotFoundException.class.getName())
                .log(Level.SEVERE, null, e);
        ExceptionDTO err = new ExceptionDTO(404, e.getMessage());
        return Response
                .status(404)
                .entity(gson.toJson(err))
                .type(MediaType.APPLICATION_JSON)
                .build();

    }
}
