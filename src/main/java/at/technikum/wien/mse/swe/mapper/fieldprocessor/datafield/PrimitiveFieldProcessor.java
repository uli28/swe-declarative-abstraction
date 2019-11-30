package at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield;

import at.technikum.wien.mse.swe.mapper.DataField;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.FieldProcessorUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

public class PrimitiveFieldProcessor implements FieldProcessorStrategy {
    @Override
    public void process(DataField dataField, Field field, Object returnObject, String fileContent, Object targetValue) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        FieldProcessorUtil.assignTargetToField(field, returnObject, castToGivenType(field, (String) targetValue));
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
