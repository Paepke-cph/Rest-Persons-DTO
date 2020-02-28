package entity.dto;

import entity.Person;

public class PersonDTO {
    private int id;
    private String firstName, lastName, phone;

    public PersonDTO(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.phone = person.getPhone();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(obj == null){
            return false;
        } else if(getClass() != obj.getClass()){
            return false;
        }
        final PersonDTO other = (PersonDTO)obj;
        if(other.getId() != this.id) {
            return false;
        }
        return true;
    }
}
