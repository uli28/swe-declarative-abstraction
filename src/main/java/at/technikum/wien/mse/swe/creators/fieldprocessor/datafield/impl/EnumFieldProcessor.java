package at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.impl;

import at.technikum.wien.mse.swe.exception.FieldProcessingException;
import at.technikum.wien.mse.swe.logger.SimpleLogger;
import at.technikum.wien.mse.swe.mapper.definitions.DataField;
import at.technikum.wien.mse.swe.creators.fieldprocessor.FieldProcessorUtil;
import at.technikum.wien.mse.swe.creators.fieldprocessor.datafield.FieldProcessorStrategy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Used for processing an enum field
 *
 * @author Ulrich Gram
 */
public class EnumFieldProcessor implements FieldProcessorStrategy {
    private static final String VALUE_OF_METHOD = "valueOf";

    @Override
    public void process(DataField dataField, Field field, Object returnObject, String fileContent, Object targetValue) {
        final Object enumObject = createEnumObject(field.getType(), (String) targetValue);
        if (enumObject != null) {
            try {
                FieldProcessorUtil.assignTargetToField(field, returnObject, enumObject);
            } catch (Exception e) {
                throw new FieldProcessingException(e);
            }
        }
    }

    @Override
    public boolean canHandle(Field field) {
        return field.getType().isEnum();
    }

    private Object createEnumObject(Class enumToCreate, String code) {
        Object object = null;
        try {
            object = Enum.valueOf(enumToCreate, code);
        } catch (Exception e) {
            SimpleLogger.debug("Exception occurred while trying to call valueOf method of enum: " + e.getMessage());
        } finally {
            if (object == null) {
                object = invokeMethodOfEnum(enumToCreate, code);
            }
        }
        return FieldProcessorUtil.parseOptional(object);
    }

    private Object invokeMethodOfEnum(Class enumToCreate, String code) {
        final List<Method> foundMethods = Arrays.stream(enumToCreate.getMethods())
                .filter(method -> !method.getName().equals(VALUE_OF_METHOD)
                        && method.getParameterCount() == 1
                        && method.getParameterTypes()[0] == String.class)
                .collect(Collectors.toList());
        if (!foundMethods.isEmpty()) {
            try {
                return foundMethods.get(0).invoke(null, code);
            } catch (IllegalAccessException | InvocationTargetException e) {
                SimpleLogger.debug("Exception occurred while instantiating method of enum: " + e.getMessage());
            }
        }
        return null;
    }
}
