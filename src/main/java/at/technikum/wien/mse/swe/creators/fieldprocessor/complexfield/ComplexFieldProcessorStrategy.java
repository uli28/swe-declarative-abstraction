package at.technikum.wien.mse.swe.creators.fieldprocessor.complexfield;

import at.technikum.wien.mse.swe.exception.FieldProcessingException;

import java.lang.reflect.Field;

/**
 * Used for finding a strategy to process a complex data field that consists of simple data fields
 *
 * @author Ulrich Gram
 */
public interface ComplexFieldProcessorStrategy {
    /**
     * Finds a way to extract information of the file content based on the parameters of a complex datafield
     * and saves it on the return object
     */
    void process(Field field, Object returnObject, String fileContent) throws FieldProcessingException;
}
