package at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.extractor.impl;

import at.technikum.wien.mse.swe.mapper.definitions.DataField;
import at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.extractor.ValueExtractor;

import static org.apache.commons.lang.StringUtils.stripStart;

/**
 * Default implementation of ValueExtractor
 *
 * @author Ulrich Gram
 */
public class DefaultValueExtractor implements ValueExtractor {

    @Override
    public String extract(DataField dataField, String fileContent) {
        if (dataField.padding().isEmpty()) {
            return extract(fileContent, dataField.startIndex(), dataField.length()).trim();
        }
        return stripStart(
                extract(fileContent, dataField.startIndex(), dataField.length()),
                dataField.padding());
    }

    private String extract(String content, int startIndex, int length) {
        return content.substring(startIndex, startIndex + length);
    }
}
