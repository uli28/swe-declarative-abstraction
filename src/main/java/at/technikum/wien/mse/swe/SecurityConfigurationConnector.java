package at.technikum.wien.mse.swe;

import java.nio.file.Path;

import at.technikum.wien.mse.swe.model.SecurityConfiguration;

/**
 * @author MatthiasKreuzriegler
 */
public interface SecurityConfigurationConnector {

    SecurityConfiguration read(Path file);

}
