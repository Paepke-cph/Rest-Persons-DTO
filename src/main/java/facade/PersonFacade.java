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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    private PersonFacade() {
    }

    /**
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }


    @Override
    public PersonsDTO getAllPersons() {
        EntityManager entityManager = getEntityManager();
        try {
            return new PersonsDTO(entityManager.createNamedQuery("Person.getAll", Person.class).getResultList());
        } finally {
            entityManager.close();
        }
    }

    @Override
    public PersonDTO editPerson(PersonDTO dto) throws PersonNotFoundException, MissingInputException {
        if(dto.getFirstName() ==  null || dto.getLastName() == null ||
                dto.getFirstName().equals("") || dto.getLastName().equals("")) {
            throw new MissingInputException();
        }
        EntityManager entityManager = getEntityManager();
        PersonDTO updated = null;
        try {
            entityManager.getTransaction().begin();
            entityManager.createNamedQuery("Person.update", Person.class)
                                            .setParameter("firstName",dto.getFirstName())
                                            .setParameter("lastName", dto.getLastName())
                                            .setParameter("phone", dto.getPhone())
                                            .setParameter("id", dto.getId()).executeUpdate();
            entityManager.getTransaction().commit();
            updated = getPerson(dto.getId());
            if(updated == null) throw new PersonNotFoundException();
        } finally {
            entityManager.close();
        }
        return updated;
    }

    @Override
    public PersonDTO deletePerson(int id) throws PersonNotFoundException{
        EntityManager entityManager = getEntityManager();
        PersonDTO person = null;
        try {
            person = getPerson(id);
            if(person != null) {
                entityManager.getTransaction().begin();
                int rows = entityManager.createNamedQuery("Person.deleteById", Person.class).setParameter("id", id).executeUpdate();
                entityManager.getTransaction().commit();
            } else {
                throw new PersonNotFoundException("Could not delete, provided id does not exist");
            }
        } finally {
            entityManager.close();
        }
        return person;
    }

    @Override
    public PersonDTO getPerson(int id) throws PersonNotFoundException {
        EntityManager entityManager = getEntityManager();
        Person person = null;
        try {
            person = entityManager.find(Person.class, id);
        } finally {
            entityManager.close();
        }
        if(person == null) {
            throw new PersonNotFoundException();
        } else {
            return new PersonDTO(person);
        }
    }

    @Override
    public PersonDTO addPerson(String firstName, String lastName, String phone, Address address) throws MissingInputException {
        if(firstName == null || lastName == null || firstName.equals("") || lastName.equals("")) {
            throw new MissingInputException();
        }
        Person person = new Person(firstName, lastName, phone);
        address.addPerson(person);
        EntityManager entityManager = getEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(person);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return new PersonDTO(person);
    }

    public int getCount() {
        EntityManager entityManager = getEntityManager();
        try {
            return Integer.parseInt(entityManager.createNamedQuery("Person.count",Long.class).getSingleResult().toString());
        } finally {
            entityManager.close();
        }
    }
}
