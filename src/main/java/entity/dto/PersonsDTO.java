package entity.dto;

import entity.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonsDTO {
    List<PersonDTO> all = new ArrayList<>();
    public PersonsDTO(List<Person> persons) {
        persons.forEach(person -> {
            all.add(new PersonDTO(person));
        });
    }

    public List<PersonDTO> getAll() {
        return all;
    }

    public void setAll(List<PersonDTO> all) {
        this.all = all;
    }
}
