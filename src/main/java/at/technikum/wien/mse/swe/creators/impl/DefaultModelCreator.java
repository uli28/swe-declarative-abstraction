package at.technikum.wien.mse.swe.creators.impl;

import at.technikum.wien.mse.swe.creators.ModelCreator;
import at.technikum.wien.mse.swe.creators.fieldprocessor.complexfield.ComplexFieldProcessorStrategy;
import at.technikum.wien.mse.swe.creators.fieldprocessor.complexfield.impl.DefaultComplexFieldProcessorStrategy;
import at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.impl.DefaultFieldProcessor;
import at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.impl.EnumFieldProcessor;
import at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.impl.PrimitiveFieldProcessor;
import at.technikum.wien.mse.swe.creators.fieldprocessor.simplefield.SimpleFieldProcessorFactory;
import at.technikum.wien.mse.swe.creators.fieldprocessor.simplefield.impl.DefaultSimpleFieldProcessorFactory;
import at.technikum.wien.mse.swe.exception.FieldProcessingException;
import at.technikum.wien.mse.swe.logger.SimpleLogger;
import at.technikum.wien.mse.swe.mapper.definitions.ComplexField;
import at.technikum.wien.mse.swe.mapper.definitions.DataField;
import at.technikum.wien.mse.swe.mapper.definitions.Mapper;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Default implementation of ModelCreator
 *
 * @param <T> the target model
 * @author Ulrich Gram
 */
public class DefaultModelCreator<T> implements ModelCreator {
    private Class mapper;
    private Class<T> target;
    private SimpleFieldProcessorFactory simpleFieldProcessorFactory;
    private ComplexFieldProcessorStrategy complexFieldProcessorStrategy;

    DefaultModelCreator(Class mapper, Class<T> target) {
        this.mapper = mapper;
        this.target = target;
        this.simpleFieldProcessorFactory = new DefaultSimpleFieldProcessorFactory(
                Arrays.asList(new PrimitiveFieldProcessor(),
                        new EnumFieldProcessor(), new DefaultFieldProcessor()));
        this.complexFieldProcessorStrategy = new DefaultComplexFieldProcessorStrategy();
    }

    @Override
    public T convert(String fileContent) {
        T targetObject = null;
        try {
            if (isMapperAnnotationPresent()) {
                targetObject = initializeObject(target);
                if (targetObject != null) {
                    mapValues(targetObject, mapper, fileContent);
                }
            }
        } catch (Exception e) {
            SimpleLogger.error("An exception occurred while performing the mapping to target model: " + e.getMessage());
        }
        return targetObject;
    }

    private boolean isMapperAnnotationPresent() {
        return mapper != null && mapper.isAnnotationPresent(Mapper.class);
    }

    private T initializeObject(Class<T> targetClass) throws IllegalAccessException, InstantiationException {
        return targetClass.newInstance();
    }

    private void mapValues(Object returnObject, Class mapper, String fileContent) throws FieldProcessingException {
        for (Field field : mapper.getDeclaredFields()) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(DataField.class)) {
                final DataField dataField = field.getAnnotation(DataField.class);
                this.simpleFieldProcessorFactory.process(dataField, field, returnObject, fileContent);

            } else if (field.isAnnotationPresent(ComplexField.class)) {
                complexFieldProcessorStrategy.process(field, returnObject, fileContent);
            }
        }
    }
}
