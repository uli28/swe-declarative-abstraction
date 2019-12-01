package at.technikum.wien.mse.swe.creators.fieldprocessor.simplefield;

import at.technikum.wien.mse.swe.exception.FieldProcessingException;
import at.technikum.wien.mse.swe.mapper.definitions.DataField;

import java.lang.reflect.Field;

/**
 * Used for finding a strategy to process a simple data field
 *
 * @author Ulrich Gram
 */
public interface SimpleFieldProcessorFactory {
    /**
     * Finds a way to extract information of the file content based on the parameters of a simple datafield
     * and saves it on the return object
     */
    void process(DataField dataField, Field field, Object returnObject, String fileContent) throws FieldProcessingException;
}
