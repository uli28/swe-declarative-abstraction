package at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.extractor;

import at.technikum.wien.mse.swe.mapper.definitions.DataField;

/**
 * Used for extracting a value based on the datafield annotation
 *
 * @author Ulrich Gram
 */
public interface ValueExtractor {
    /**
     * Used for extracting a value out of file content based on the datafield annotation
     *
     * @param dataField   the datafield containing the information on how to extract the values
     * @param fileContent the source content that holds the information
     * @return the extracted value
     */
    String extract(DataField dataField, String fileContent);
}
