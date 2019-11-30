package at.technikum.wien.mse.swe.mapper.fieldprocessor.simplefield;

import at.technikum.wien.mse.swe.mapper.DataField;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public interface SimpleFieldProcessorFactory {
    void process(DataField dataField, Field field, Object returnObject, String fileContent) throws InvocationTargetException, NoSuchMethodException, NoSuchFieldException, InstantiationException, IllegalAccessException;
}
