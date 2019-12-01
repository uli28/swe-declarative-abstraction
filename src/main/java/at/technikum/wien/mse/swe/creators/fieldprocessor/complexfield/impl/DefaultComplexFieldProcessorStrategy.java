package at.technikum.wien.mse.swe.creators.fieldprocessor.complexfield.impl;

import at.technikum.wien.mse.swe.creators.fieldprocessor.FieldProcessorUtil;
import at.technikum.wien.mse.swe.creators.fieldprocessor.complexfield.ComplexFieldProcessorStrategy;
import at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.impl.DefaultFieldProcessor;
import at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.impl.EnumFieldProcessor;
import at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.impl.PrimitiveFieldProcessor;
import at.technikum.wien.mse.swe.creators.fieldprocessor.simplefield.SimpleFieldProcessorFactory;
import at.technikum.wien.mse.swe.creators.fieldprocessor.simplefield.impl.DefaultSimpleFieldProcessorFactory;
import at.technikum.wien.mse.swe.exception.FieldProcessingException;
import at.technikum.wien.mse.swe.mapper.definitions.ComplexField;
import at.technikum.wien.mse.swe.mapper.definitions.DataField;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Default implementation of ComplexFieldProcessorStrategy
 *
 * @author Ulrich Gram
 */
public class DefaultComplexFieldProcessorStrategy implements ComplexFieldProcessorStrategy {
    private SimpleFieldProcessorFactory simpleFieldProcessorFactory;

    public DefaultComplexFieldProcessorStrategy() {
        this.simpleFieldProcessorFactory = new DefaultSimpleFieldProcessorFactory(
                Arrays.asList(new PrimitiveFieldProcessor(),
                        new EnumFieldProcessor(), new DefaultFieldProcessor()));
    }

    @Override
    public void process(Field field, Object returnObject, String fileContent) throws FieldProcessingException {
        final ComplexField complexField = field.getAnnotation(ComplexField.class);
        final DataField[] primitiveFields = complexField.fieldMapper();
        final Class<?> typeOfClass = field.getType();
        try {
            final Object targetObject = FieldProcessorUtil.createNewInstance(typeOfClass);

            for (final DataField dataField : primitiveFields) {
                final String name = dataField.name();
                final Field fieldOfComposedObject = typeOfClass.getDeclaredField(name);

                simpleFieldProcessorFactory.process(dataField, fieldOfComposedObject, targetObject, fileContent);
            }
            FieldProcessorUtil.assignTargetToField(field, returnObject, targetObject);
        } catch (Exception e) {
            throw new FieldProcessingException(e);
        }
    }
}
