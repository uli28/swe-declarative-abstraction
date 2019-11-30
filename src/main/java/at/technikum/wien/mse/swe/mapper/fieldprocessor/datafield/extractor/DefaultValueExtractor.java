package at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield.extractor;

import at.technikum.wien.mse.swe.mapper.DataField;

import static org.apache.commons.lang.StringUtils.stripStart;

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
