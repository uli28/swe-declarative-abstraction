package at.technikum.wien.mse.swe.exception;

/**
 * @author Ulrich Gram
 */

public class FieldProcessingException extends RuntimeException {
    public FieldProcessingException(Exception e) {
        super(e);
    }
}
