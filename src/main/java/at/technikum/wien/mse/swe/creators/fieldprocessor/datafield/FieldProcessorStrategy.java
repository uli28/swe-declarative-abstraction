package at.technikum.wien.mse.swe.creators.fieldprocessor.datafield;

import at.technikum.wien.mse.swe.exception.FieldProcessingException;
import at.technikum.wien.mse.swe.mapper.definitions.DataField;

import java.lang.reflect.Field;

/**
 * Used for processing a certain type of field and saving the information on the return object
 *
 * @author Ulrich Gram
 */
public interface FieldProcessorStrategy {
    /**
     * Used for processing file content based on the given parameters and saving it on the return object
     * @param dataField the datafield annotation used for extracting information
     * @param field the target field
     * @param returnObject the object where the information should be stored
     * @param fileContent the content of the source
     * @param targetValue the value that should be saved
     * @throws FieldProcessingException
     */
    void process(DataField dataField, Field field, Object returnObject, String fileContent, Object targetValue) throws FieldProcessingException;

    /**
     * Evaluates whether a certain processor is able to process the given field
     * @param field the field to process
     * @return true if yes, false if not
     */
    boolean canHandle(Field field);
}
