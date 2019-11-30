package at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield;

import at.technikum.wien.mse.swe.mapper.DataField;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.FieldProcessorUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumFieldProcessor implements FieldProcessorStrategy {
    private static final String VALUE_OF_METHOD = "valueOf";

    @Override
    public void process(DataField dataField, Field field, Object returnObject, String fileContent, Object targetValue) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        final Object enumObject = createEnumObject(field.getType(), (String) targetValue);
        if (enumObject != null) {
            FieldProcessorUtil.assignTargetToField(field, returnObject, enumObject);
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
            final List<Method> foundMethods = Arrays.stream(enumToCreate.getMethods())
                    .filter(method -> !method.getName().equals(VALUE_OF_METHOD)
                            && method.getParameterCount() == 1
                            && method.getParameterTypes()[0] == String.class)
                    .collect(Collectors.toList());
            if (!foundMethods.isEmpty()) {
                try {
                    object = foundMethods.get(0).invoke(null, code);
                } catch (IllegalAccessException | InvocationTargetException exception) {
                    System.out.println(exception);
                }
            }
        }
        return FieldProcessorUtil.parseOptional(object);
    }
}
