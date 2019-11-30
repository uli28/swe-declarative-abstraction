package at.technikum.wien.mse.swe.converter.impl;

import at.technikum.wien.mse.swe.converter.MapperConverter;
import at.technikum.wien.mse.swe.mapper.ComplexField;
import at.technikum.wien.mse.swe.mapper.DataField;
import at.technikum.wien.mse.swe.mapper.Mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang.StringUtils.stripStart;

public class DefaultMapperConverter<T> implements MapperConverter {
    private static final String VALUE_OF_METHOD = "valueOf";
    private Class mapper;
    private Class<T> target;

    DefaultMapperConverter(Class mapper, Class<T> target) {
        this.mapper = mapper;
        this.target = target;
    }

    @Override
    public T convert(String fileContent) {
        T targetObject = null;
        try {
            if (isMapperAnnotationPresent()) {
                targetObject = initializeObject(target);
                if (targetObject != null) {
                    updateValuesFromAnnotations(targetObject, mapper, fileContent);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return targetObject;
    }

    private boolean isMapperAnnotationPresent() {
        return mapper != null && mapper.isAnnotationPresent(Mapper.class);
    }

    private T initializeObject(Class<T> targetClass) throws IllegalAccessException, InstantiationException {
        return targetClass.newInstance();
    }

    private void updateValuesFromAnnotations(Object returnObject, Class mapper, String fileContent) throws NoSuchFieldException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        for (Field field : mapper.getDeclaredFields()) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(DataField.class)) {
                final DataField dataField = field.getAnnotation(DataField.class);
                processPrimitiveField(dataField, field, returnObject, fileContent);
            } else if (field.isAnnotationPresent(ComplexField.class)) {
                processComposedField(field, returnObject, fileContent);
            }
        }
    }

    private void processComposedField(Field field, Object returnObject, String fileContent) throws NoSuchFieldException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        final ComplexField complexField = field.getAnnotation(ComplexField.class);
        final DataField[] primitiveFields = complexField.fieldMapper();
        final Class<?> typeOfClass = field.getType();
        final Object targetObject = createNewInstance(typeOfClass);

        for (final DataField dataField : primitiveFields) {
            final String name = dataField.name();
            final Field fieldOfComposedObject = typeOfClass.getDeclaredField(name);
            processPrimitiveField(dataField, fieldOfComposedObject, targetObject, fileContent);
        }
        assignTargetToField(field, returnObject, targetObject);
    }

    private void processPrimitiveField(DataField dataField, Field field, Object returnObject, String fileContent) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        final String targetValue = processDataField(dataField, fileContent);

        if (field.getType().isPrimitive() || field.getType().equals(String.class)
                || field.getType().equals(BigDecimal.class)) {
            assignTargetToField(field, returnObject, castToGivenType(field, targetValue));

        } else if (field.getType().isEnum()) {
            final Object enumObject = createEnumObject(field.getType(), targetValue);
            if (enumObject != null) {
                assignTargetToField(field, returnObject, enumObject);
            }

        } else {
            final Class<?> typeOfClass = field.getType();
            final Object targetObject = createNewInstance(typeOfClass);

            final String name = dataField.name();
            final Field fieldOfComposedObject = typeOfClass.getDeclaredField(name);
            processPrimitiveField(dataField, fieldOfComposedObject, targetObject, fileContent);

            assignTargetToField(field, returnObject, targetObject);
        }
    }

    private Object castToGivenType(Field field, String targetValue) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        final Class<?> classOfField = field.getType();
        if (BigDecimal.class.equals(classOfField)) {
            return BigDecimal.valueOf(Double.valueOf(targetValue));
        }
        final Constructor<?> constructor = classOfField.getConstructor(String.class);
        return constructor.newInstance(targetValue);
    }

    private void assignTargetToField(Field field, Object returnObject, Object targetValue) throws IllegalAccessException {
        Field fieldOnReturnObject = findField(field, returnObject, targetValue);
        fieldOnReturnObject.setAccessible(true);
        fieldOnReturnObject.set(returnObject, targetValue);
    }

    private Field findField(Field field, Object returnObject, Object targetValue) {
        Field fieldOnReturnObject = null;
        try {
            fieldOnReturnObject = returnObject.getClass().getDeclaredField(field.getName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
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

    private String processDataField(DataField dataField, String fileContent) {
        if (dataField.padding().isEmpty()) {
            return extract(fileContent, dataField.startIndex(), dataField.length()).trim();
        }
        return stripStart(
                extract(fileContent, dataField.startIndex(), dataField.length()),
                dataField.padding());
    }

    private String extract(String content, int startIndex, int length) {
        return content.substring(startIndex, startIndex + length);
    }

    private Object createNewInstance(Class<?> typeOfClass) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        try {
            return typeOfClass.newInstance();
        } catch (Exception e) {
            System.out.println("could not instantiate: " + typeOfClass.getName());
        }
        return createObjectPerConstructor(typeOfClass);
    }

    private Object createObjectPerConstructor(Class<?> typeOfClass) throws
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
        return parseOptional(object);
    }

    private Object parseOptional(Object object) {
        if (object == null || object.getClass() != Optional.class) {
            return object;
        }
        final Optional optional = (Optional) object;
        if (!optional.isPresent()) {
            return null;
        }
        return optional.get();
    }
}
