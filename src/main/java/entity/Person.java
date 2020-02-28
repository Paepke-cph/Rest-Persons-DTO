package entity;
/*
 * author paepke
 * version 1.0
 */

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.eclipse.persistence.jpa.jpql.tools.model.ICaseExpressionStateObjectBuilder;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


@Entity
@Table(name = "Person")
@NamedNativeQuery(name = "Person.Truncate", query = "TRUNCATE TABLE Person")
@NamedQueries({
    @NamedQuery(name = "Person.deleteAllRows", query = "DELETE FROM Person"),
    @NamedQuery(name = "Person.count", query = "SELECT COUNT(p) FROM Person p"),
    @NamedQuery(name = "Person.getAll", query = "SELECT p FROM Person p"),
    @NamedQuery(name = "Person.findByFirstName", query = "SELECT p FROM Person p WHERE p.firstName LIKE :name"),
    @NamedQuery(name = "Person.findByLastName", query = "SELECT p FROM Person p WHERE p.lastName LIKE :name"),
    @NamedQuery(name = "Person.findByPhone", query = "SELECT p FROM Person p WHERE p.phone LIKE :phone"),
    @NamedQuery(name = "Person.deleteById", query = "DELETE FROM Person p WHERE p.id = :id"),
    @NamedQuery(name = "Person.update",
            query = "UPDATE Person p " +
                    "SET p.firstName = :firstName, " +
                    "p.lastName = :lastName, " +
                    "p.phone = :phone WHERE p.id = :id")
})
public class Person implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String firstName;
    private String lastName;
    private String phone;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastEdited;

    @ManyToOne
    private Address address;

    public Person() {
    }

    public Person(String firstName, String lastName, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    @PrePersist
    protected void onCreate() {
        lastEdited = created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        lastEdited = new Date();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Date getCreated() { return created; }
    public void setCreated(Date created) { this.created = created; }
    public Date getLastEdited() { return lastEdited; }
    public void setLastEdited(Date lastEdited) { this.lastEdited = lastEdited; }
    public Address getAddress() { return address; }
    public void setAddress(Address address) {
        this.address = address;
        address.addPerson(this);
    }
}
