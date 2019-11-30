package at.technikum.wien.mse.swe.mapper.fieldprocessor.complexfield;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public interface ComplexFieldProcessorStrategy {
    void process(Field field, Object returnObject, String fileContent) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException;
}
