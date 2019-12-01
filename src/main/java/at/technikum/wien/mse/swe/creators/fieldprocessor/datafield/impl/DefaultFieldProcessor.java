package at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.impl;

import at.technikum.wien.mse.swe.exception.FieldProcessingException;
import at.technikum.wien.mse.swe.mapper.definitions.DataField;
import at.technikum.wien.mse.swe.creators.fieldprocessor.FieldProcessorUtil;
import at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.FieldProcessorStrategy;
import at.technikum.wien.mse.swe.creators.fieldprocessor.simplefield.impl.DefaultSimpleFieldProcessorFactory;
import at.technikum.wien.mse.swe.creators.fieldprocessor.simplefield.SimpleFieldProcessorFactory;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Used for processing a primitive field that is part of a class and therefore needs to be saved on the target object
 *
 * @author Ulrich Gram
 */
public class DefaultFieldProcessor implements FieldProcessorStrategy {

    private SimpleFieldProcessorFactory simpleFieldProcessorFactory;

    public DefaultFieldProcessor() {
        simpleFieldProcessorFactory = new DefaultSimpleFieldProcessorFactory(
                Arrays.asList(new PrimitiveFieldProcessor(), new EnumFieldProcessor())
        );
    }

    @Override
    public void process(DataField dataField, Field field, Object returnObject, String fileContent, Object targetValue) {

        try {
            final Class<?> typeOfClass = field.getType();
            final Object targetObject = FieldProcessorUtil.createNewInstance(typeOfClass);

            final String name = dataField.name();
            final Field fieldOfComposedObject = typeOfClass.getDeclaredField(name);
            processPrimitiveField(dataField, fieldOfComposedObject, targetObject, fileContent);

            FieldProcessorUtil.assignTargetToField(field, returnObject, targetObject);
        } catch (Exception e) {
            throw new FieldProcessingException(e);
        }
    }

    private void processPrimitiveField(DataField dataField, Field fieldOfComposedObject, Object returnObject, String fileContent) {
        simpleFieldProcessorFactory.process(dataField, fieldOfComposedObject, returnObject, fileContent);
    }


    @Override
    public boolean canHandle(Field field) {
        return true;
    }
}
