package at.technikum.wien.mse.swe.mapper.fieldprocessor.simplefield;

import at.technikum.wien.mse.swe.mapper.DataField;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield.DefaultFieldProcessor;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield.FieldProcessorStrategy;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield.extractor.DefaultValueExtractor;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield.extractor.ValueExtractor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

public class DefaultSimpleFieldProcessorFactory implements SimpleFieldProcessorFactory {

    private final List<FieldProcessorStrategy> strategies;
    private ValueExtractor valueExtractor;

    public DefaultSimpleFieldProcessorFactory(List<FieldProcessorStrategy> fieldProcessorStrategyList) {
        this.strategies = fieldProcessorStrategyList;
        this.valueExtractor = new DefaultValueExtractor();
    }

    @Override
    public void process(DataField dataField, Field field, Object returnObject, String fileContent) throws InvocationTargetException, NoSuchMethodException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        final String targetValue = valueExtractor.extract(dataField, fileContent);
        final Optional<FieldProcessorStrategy> foundStrategy = strategies.stream()
                .filter(strategy -> strategy.canHandle(field))
                .findFirst();

        if (foundStrategy.isPresent()) {
            foundStrategy.get().process(dataField, field, returnObject, fileContent, targetValue);
        } else {
            new DefaultFieldProcessor().process(dataField, field, returnObject, fileContent, targetValue);
        }
    }
}
