package rest;
/*
 * author paepke
 * version 1.0
 */

import entity.Person;
import entity.dto.PersonDTO;
import exception.MissingInputException;
import exception.PersonNotFoundException;
import utils.EMF_Creator;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

import io.restassured.parsing.Parser;

import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;


public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Person r1, r2;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.CREATE);

        httpServer = startServer();
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        r1 = new Person("Lars", "Larsen", "1111");
        r2 = new Person("Niels", "Nielsen", "2222");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.Truncate").executeUpdate();
            em.persist(r1);
            em.persist(r2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testInvalid_endpoint() {
        given()
                .when()
                .get("cars")
                .then()
                .statusCode(404)
                .body("code", equalTo(404),
                        "message", equalTo("Not Found"));
    }

    @Test
    public void testDeletePerson_with_invalid_id() {
        int id = 100;
        when()
                .delete("person/{id}",id)
                .then()
                .statusCode(404)
                .body("code", equalTo(404),
                        "message", equalTo(PersonNotFoundException.DEFAULT_MESSAGE));
    }

    @Test
    public void testDeletePerson_with_valid_id() {
        PersonDTO person =
                when()
                .delete("person/{id}",r1.getId())
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getObject("", PersonDTO.class);
        assertEquals(person, new PersonDTO(r1));
    }

    @Test
    public void testUpdatePerson_with_missing_input() {
        PersonDTO person = new PersonDTO("Niels", "", "4444");
        person.setId(1);
        given()
                .contentType("application/json")
                .body(person)
                .when()
                .put("person")
                .then()
                .statusCode(400)
                .body("code", equalTo(400),
                        "message", equalTo(MissingInputException.DEFAULT_MESSAGE));
    }

    @Test
    public void testUpdatePerson() {
        PersonDTO person = new PersonDTO("Niels", "Larsen", "4444");
        person.setId(1); // Update person with id = 1
        given()
                .contentType("application/json")
                .body(person)
                .when()
                .put("person")
                .then()
                .statusCode(200)
                .body("firstName",equalTo("Niels"),
                        "lastName",equalTo("Larsen"),
                        "phone", equalTo("4444"),
                        "id", equalTo(1));
    }

    @Test
    public void testCreatePerson_with_missing_input() {
        PersonDTO person = new PersonDTO("Benny", "", "3333");
        given()
                .contentType("application/json")
                .body(person)
                .when()
                .post("person")
                .then()
                .statusCode(400)
                .body("code", equalTo(400),
                        "message", equalTo(MissingInputException.DEFAULT_MESSAGE));
    }

    @Test
    public void testCreatePerson() {
        PersonDTO person = new PersonDTO("Benny", "Hill", "3333");
        given()
                .contentType("application/json")
                .body(person)
                .when()
                .post("person")
                .then()
                .body("firstName", equalTo("Benny"),
                        "lastName", equalTo("Hill"),
                        "phone", equalTo("3333"),
                        "id", notNullValue());
    }

    @Test
    public void testGetAll() {
        List<PersonDTO> persons =
                when().get("person/all")
                .then()
                .extract().body().jsonPath().getList("all", PersonDTO.class);
        PersonDTO pdto = new PersonDTO(r1);
        PersonDTO p2dto = new PersonDTO(r2);
        assertThat(persons, containsInAnyOrder(pdto,p2dto));
    }


    @Test
    public void testGetById_with_invalid_id() {
        int id = 100;
        when().get("person/{id}", id)
                .then()
                .statusCode(404)
                .body("code",equalTo(404),
                        "message", equalTo(PersonNotFoundException.DEFAULT_MESSAGE));
    }

    @Test
    public void testGetById_with_valid_id() {
        when().get("person/{id}", r1.getId())
                .then()
                .statusCode(200)
                .body("firstName", equalTo("Lars"),
                        "lastName", equalTo("Larsen"),
                        "phone", equalTo("1111"));
    }

    @Test
    public void testServerIsUp() {
        given().when().get("/person").then().statusCode(200);
    }
}
