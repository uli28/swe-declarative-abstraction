package at.technikum.wien.mse.swe.creators.fieldprocessor.simplefield.impl;

import at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.FieldProcessorStrategy;
import at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.extractor.ValueExtractor;
import at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.extractor.impl.DefaultValueExtractor;
import at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.impl.DefaultFieldProcessor;
import at.technikum.wien.mse.swe.creators.fieldprocessor.simplefield.SimpleFieldProcessorFactory;
import at.technikum.wien.mse.swe.mapper.definitions.DataField;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of SimpleFieldProcessorFactory
 *
 * @author Ulrich Gram
 */
public class DefaultSimpleFieldProcessorFactory implements SimpleFieldProcessorFactory {

    private final List<FieldProcessorStrategy> strategies;
    private ValueExtractor valueExtractor;

    public DefaultSimpleFieldProcessorFactory(List<FieldProcessorStrategy> fieldProcessorStrategyList) {
        this.strategies = fieldProcessorStrategyList;
        this.valueExtractor = new DefaultValueExtractor();
    }

    @Override
    public void process(DataField dataField, Field field, Object returnObject, String fileContent) {
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
