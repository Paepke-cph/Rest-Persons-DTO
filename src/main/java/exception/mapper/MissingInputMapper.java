package exception.mapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exception.MissingInputException;
import exception.PersonNotFoundException;
import exception.dto.ExceptionDTO;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class MissingInputMapper implements ExceptionMapper<MissingInputException> {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public Response toResponse(MissingInputException e) {
        Logger.getLogger(MissingInputException.class.getName())
                .log(Level.SEVERE, null, e);
        ExceptionDTO err = new ExceptionDTO(400, e.getMessage());
        return Response
                .status(400)
                .entity(gson.toJson(err))
                .type(MediaType.APPLICATION_JSON)
                .build();

    }
}
