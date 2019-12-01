package at.technikum.wien.mse.swe.creators;

/**
 * Creates a model based on a given mapper and the content of a file
 *
 * @param <T> the target class that should be created
 * @author Ulrich Gram
 */
public interface ModelCreator<T> {

    /**
     * Maps the file content to the target model
     *
     * @param fileContent the content of the file
     * @return the target model containing the data
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    T convert(String fileContent) throws IllegalAccessException, InstantiationException;
}
