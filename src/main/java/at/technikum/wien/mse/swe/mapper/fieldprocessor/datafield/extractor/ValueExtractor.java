package at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield.extractor;

import at.technikum.wien.mse.swe.mapper.DataField;

public interface ValueExtractor {
    String extract(DataField dataField, String fileContent);
}
