package at.technikum.wien.mse.swe.creators.fieldprocessor;

import at.technikum.wien.mse.swe.logger.SimpleLogger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * FieldProcessUtil used for various operations that help to fulfill the mapping from the mapper object to the target object
 */
public class FieldProcessorUtil {
    public static void assignTargetToField(Field field, Object returnObject, Object targetValue) throws IllegalAccessException {
        Field fieldOnReturnObject = findField(field, returnObject, targetValue);
        fieldOnReturnObject.setAccessible(true);
        fieldOnReturnObject.set(returnObject, targetValue);
    }

    public static Object parseOptional(Object object) {
        if (object == null || object.getClass() != Optional.class) {
            return object;
        }
        final Optional optional = (Optional) object;
        if (!optional.isPresent()) {
            return null;
        }
        return optional.get();
    }

    private static Field findField(Field field, Object returnObject, Object targetValue) {
        Field fieldOnReturnObject = null;
        try {
            fieldOnReturnObject = returnObject.getClass().getDeclaredField(field.getName());
        } catch (Exception e) {
            SimpleLogger.debug("Exception occurred while getting the declared field, will search by type now");
        }
        if (fieldOnReturnObject == null) {
            final Field[] declaredFields = returnObject.getClass().getDeclaredFields();
            for (Field fieldToFind : declaredFields) {
                if (fieldToFind.getType().equals(targetValue.getClass())) {
                    return fieldToFind;
                }
            }
        }
        return fieldOnReturnObject;
    }

    public static Object createNewInstance(Class<?> typeOfClass) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        try {
            return typeOfClass.newInstance();
        } catch (Exception e) {
            SimpleLogger.debug("could not instantiate: " + typeOfClass.getName() + ", trying constructor instantiation now");
        }
        return createObjectPerConstructor(typeOfClass);
    }

    private static Object createObjectPerConstructor(Class<?> typeOfClass) throws
            NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Field[] fields = typeOfClass.getDeclaredFields();
        final List<Class> types = new ArrayList<>();
        final List<Object> values = new ArrayList<>();
        for (Field fieldToGetClassFrom : fields) {
            types.add(fieldToGetClassFrom.getType());
            values.add(null);
        }

        final Class[] classes = new Class[types.size()];
        final Object[] objects = new Object[values.size()];
        final Constructor<?> constructor = typeOfClass.getConstructor(types.toArray(classes));
        return constructor.newInstance(objects);
    }
}
