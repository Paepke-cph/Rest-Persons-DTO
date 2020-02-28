package facade;
/*
 * author paepke
 * version 1.0
 */

import entity.Address;
import entity.Person;
import entity.dto.PersonDTO;
import entity.dto.PersonsDTO;
import exception.MissingInputException;
import exception.PersonNotFoundException;
import utils.EMF_Creator;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade personfacade;

    private static final Address ADDRESS_TEST = new Address("TestStreet", "TestCity","TestZip");

    private Person p1, p2;
    private Address address, address2;
    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClassV2() {
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.DROP_AND_CREATE);
        personfacade = PersonFacade.getPersonFacade(emf);
    }

    @BeforeEach
    public void setUp() {
        address = new Address("Langgade", "Copenhagen","9999");
        address2 = new Address("Kortgade", "Copenhagen", "9998");
        p1 = new Person("Peter", "Petersen", "1111");
        address.addPerson(p1);
        p2 = new Person("Lars", "Larsen", "2222");
        address2.addPerson(p2);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.Truncate").executeUpdate();
            em.persist(p1);
            em.getTransaction().commit();
            em.getTransaction().begin();
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testGetAllPerson() {
        PersonsDTO persons = personfacade.getAllPersons();
        assertEquals(2, persons.getAll().size());
        PersonDTO dto1 = persons.getAll().get(0);
        PersonDTO dto2 = persons.getAll().get(1);

        assertEquals("Peter", dto1.getFirstName());
        assertEquals("Petersen", dto1.getLastName());
        assertEquals("1111", dto1.getPhone());
        assertEquals("Lars", dto2.getFirstName());
        assertEquals("Larsen", dto2.getLastName());
        assertEquals("2222", dto2.getPhone());
    }

    @Test
    public void testEditPerson_with_missing_input() {
        assertThrows(MissingInputException.class, () -> {
            PersonDTO person = new PersonDTO("","","1111", ADDRESS_TEST);
            personfacade.editPerson(person);
        });
    }

    @Test
    public void testEditPerson_with_valid_id() throws PersonNotFoundException, MissingInputException {
        PersonDTO personDTO = new PersonDTO("FN","Petersen","1111", address);
        personDTO.setId(1);
        PersonDTO updated = personfacade.editPerson(personDTO);
        assertEquals(1, updated.getId());
        assertEquals("FN", updated.getFirstName());
        assertEquals("Petersen", updated.getLastName());
        assertEquals("1111", updated.getPhone());
    }

    @Test
    public void testDeletePerson_with_invalid_id() {
        int id = 1000;
        assertThrows(PersonNotFoundException.class, () -> personfacade.deletePerson(id));
    }

    @Test
    public void testDeletePerson_with_valid_id() throws PersonNotFoundException {
        int id = 1;
        PersonDTO person = personfacade.deletePerson(id);
        assertEquals("Peter", person.getFirstName());
        assertEquals("Petersen", person.getLastName());
        assertEquals("1111", person.getPhone());
    }

    @Test
    public void testGetPerson_with_invalid_id() {
        int id = 1000;
        assertThrows(PersonNotFoundException.class,() -> personfacade.getPerson(id));
    }

    @Test
    public void testGetPerson_with_valid_id() throws PersonNotFoundException {
        int id = 1;
        PersonDTO person = personfacade.getPerson(id);
        assertEquals("Peter", person.getFirstName());
        assertEquals("Petersen", person.getLastName());
        assertEquals("1111", person.getPhone());
    }

    @Test
    public void testAddPerson_with_missing_input() {
        assertThrows(MissingInputException.class, () -> {
            personfacade.addPerson(null,"","3333", ADDRESS_TEST);
        });
    }

    @Test
    public void testAddPerson() throws MissingInputException {
        PersonDTO created = personfacade.addPerson("Bo", "Nielsen", "3333", ADDRESS_TEST);
        assertEquals(3, created.getId());
        assertEquals("Bo", created.getFirstName());
        assertEquals("Nielsen", created.getLastName());
        assertEquals("3333", created.getPhone());
    }

    @Test
    public void testAFacadeMethod() {
        assertEquals(2, personfacade.getCount(), "Expects two rows in the database");
    }
}
