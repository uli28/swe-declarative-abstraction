package at.technikum.wien.mse.swe.exception;

/**
 * @author MatthiasKreuzriegler
 */
public class SecurityAccountOverviewReadException extends RuntimeException {

    public SecurityAccountOverviewReadException(Exception e) {
        super(e);
    }
}
