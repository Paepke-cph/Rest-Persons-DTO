package facade;

import entity.dto.PersonDTO;
import exception.PersonNotFoundException;

import java.util.List;

public interface IPersonFacade {
    PersonDTO addPerson(String firstName, String lastName, String phone);
    PersonDTO deletePerson(int id) throws PersonNotFoundException;
    PersonDTO getPerson(int id) throws PersonNotFoundException;
    List<PersonDTO> getAllPersons();
    PersonDTO editPerson(PersonDTO dto) throws PersonNotFoundException;
}
