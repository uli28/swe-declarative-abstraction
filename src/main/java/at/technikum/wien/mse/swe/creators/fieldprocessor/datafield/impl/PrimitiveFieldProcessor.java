package at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.impl;

import at.technikum.wien.mse.swe.exception.FieldProcessingException;
import at.technikum.wien.mse.swe.mapper.definitions.DataField;
import at.technikum.wien.mse.swe.creators.fieldprocessor.FieldProcessorUtil;
import at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.FieldProcessorStrategy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

/**
 * Used for processing a primitive field that isn't part of a class and stands for its own
 *
 * @author Ulrich Gram
 */
public class PrimitiveFieldProcessor implements FieldProcessorStrategy {
    @Override
    public void process(DataField dataField, Field field, Object returnObject, String fileContent, Object targetValue) {
        try {
            FieldProcessorUtil.assignTargetToField(field, returnObject, castToGivenType(field, (String) targetValue));
        } catch (Exception e) {
            throw new FieldProcessingException(e);
        }
    }

    @Override
    public boolean canHandle(Field field) {
        if (field.getType().isPrimitive() || field.getType().equals(String.class)
                || field.getType().equals(BigDecimal.class)) {
            return true;
        }
        return false;
    }

    private Object castToGivenType(Field field, String targetValue) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        final Class<?> classOfField = field.getType();
        if (BigDecimal.class.equals(classOfField)) {
            return BigDecimal.valueOf(Double.valueOf(targetValue));
        }
        final Constructor<?> constructor = classOfField.getConstructor(String.class);
        return constructor.newInstance(targetValue);
    }
}
