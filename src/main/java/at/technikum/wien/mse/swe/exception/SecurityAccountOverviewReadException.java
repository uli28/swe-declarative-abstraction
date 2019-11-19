package at.technikum.wien.mse.swe.exception;

import java.io.IOException;

/**
 * @author MatthiasKreuzriegler
 */
public class SecurityAccountOverviewReadException extends RuntimeException {

    public SecurityAccountOverviewReadException(Exception e) {
        super(e);
    }
}
