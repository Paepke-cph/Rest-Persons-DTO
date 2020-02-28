package entity.dto;

import entity.Address;
import entity.Person;

import javax.json.bind.annotation.JsonbTransient;
import java.beans.Transient;

public class PersonDTO {
    private int id;
    private String firstName, lastName, phone;
    @JsonbTransient
    private Address address;

    public PersonDTO(String firstName, String lastName, String phone, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }

    public PersonDTO(Person person) {
        this.id = person.getId();
        this.firstName = person.getFirstName();
        this.lastName = person.getLastName();
        this.phone = person.getPhone();
        this.address = person.getAddress();
    }

    public int getId() { return id; }
    public void setId(int id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }

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
