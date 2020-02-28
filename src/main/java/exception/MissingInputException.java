package exception;

public class MissingInputException extends Exception {
    public static final String DEFAULT_MESSAGE = "First Name and/or Last Name is missing";
    public MissingInputException() {
        super(DEFAULT_MESSAGE);
    }
    public MissingInputException(String message) {
        super(message);
    }
}
