package projectbackend.modelo.excepciones;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
