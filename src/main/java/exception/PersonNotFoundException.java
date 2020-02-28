package exception;

public class PersonNotFoundException extends Exception {
    public static final String DEFAULT_MESSAGE = "No person with provided id found";

    public PersonNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
    public PersonNotFoundException(String message) {
        super(message);
    }
}
