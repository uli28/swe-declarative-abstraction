package at.technikum.wien.mse.swe.creators;

import java.nio.file.Path;

/**
 * Connector that maps the given input to the target model
 *
 * @param <T> the target model
 * @author Ulrich Gram
 */
public interface ConnectorFactory<T> {

    /**
     * Reads the content of a file and maps it to the target model
     *
     * @param file the path of the file which content should be mapped
     * @return the target model containing the information
     */
    T read(Path file);
}
