package fg.eternity.validator;

/**
 * Exception used for validation errors
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
