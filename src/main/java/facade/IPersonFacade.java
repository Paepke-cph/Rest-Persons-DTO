package facade;

import entity.dto.PersonDTO;

import java.util.List;

public interface IPersonFacade {
    PersonDTO addPerson(String firstName, String lastName, String phone);
    PersonDTO deletePerson(int id);
    PersonDTO getPerson(int id);
    List<PersonDTO> getAllPersons();
    PersonDTO editPerson(PersonDTO dto);
}
