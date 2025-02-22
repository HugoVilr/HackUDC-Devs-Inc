package projectbackend.modelo.excepciones;

public class ResourceNotFoundEception extends RuntimeException {
    public ResourceNotFoundEception(String message) {
        super(message);
    }
}
