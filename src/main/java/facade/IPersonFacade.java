package facade;

import entity.Address;
import entity.dto.PersonDTO;
import entity.dto.PersonsDTO;
import exception.MissingInputException;
import exception.PersonNotFoundException;

public interface IPersonFacade {
    PersonDTO addPerson(String firstName, String lastName, String phone, Address address) throws MissingInputException;
    PersonDTO deletePerson(int id) throws PersonNotFoundException;
    PersonDTO getPerson(int id) throws PersonNotFoundException;
    PersonsDTO getAllPersons();
    PersonDTO editPerson(PersonDTO dto) throws PersonNotFoundException, MissingInputException;
}
