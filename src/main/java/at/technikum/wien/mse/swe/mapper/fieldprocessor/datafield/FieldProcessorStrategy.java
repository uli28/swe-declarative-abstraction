package at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield;

import at.technikum.wien.mse.swe.mapper.DataField;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public interface FieldProcessorStrategy {
    void process(DataField dataField, Field field, Object returnObject, String fileContent, Object targetValue) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException;

    boolean canHandle(Field field);
}
