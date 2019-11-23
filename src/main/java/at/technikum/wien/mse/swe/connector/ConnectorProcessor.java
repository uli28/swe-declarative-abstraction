package at.technikum.wien.mse.swe.connector;

import at.technikum.wien.mse.swe.exception.SecurityAccountOverviewReadException;
import at.technikum.wien.mse.swe.model.RiskCategory;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.apache.commons.lang.StringUtils.stripStart;

public class ConnectorProcessor {
    public Object convertFileToModel(Object connector, Path file) {
        Object returnObject = null;
        try {
            checkIfConnector(connector);
            returnObject = initializeObject(connector, file);
            if (returnObject != null) {
                updateValuesFromAnnotations(returnObject, connector, file);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return returnObject;
    }

    private void updateValuesFromAnnotations(Object returnObject, Object connector, Path file) throws NoSuchFieldException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        Class<?> clazz = connector.getClass();
        final String fileContent = getStringOfFile(file);
        for (Field field : clazz.getDeclaredFields()) {
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
        Object targetObject = createNewInstance(typeOfClass);

        for (final DataField dataField : primitiveFields) {
            final String name = dataField.name();
            final Field fieldOfComposedObject = typeOfClass.getDeclaredField(name);
            processPrimitiveField(dataField, fieldOfComposedObject, targetObject, fileContent);
        }
        assignTargetToField(field, returnObject, targetObject);
    }

    private void processPrimitiveField(DataField dataField, Field field, Object returnObject, String fileContent) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException {
        final String targetValue = processDataField(field, dataField, fileContent);

        if (field.getType().isPrimitive() || field.getType().equals(String.class)
                || field.getType().equals(BigDecimal.class)) {
            assignTargetToField(field, returnObject, castToGivenType(field, targetValue));

        } else if (field.getType().isEnum()) {
            if (field.getType().equals(RiskCategory.class)) {
                Optional<RiskCategory> riskCategory = RiskCategory.fromCode(targetValue);
                if (riskCategory.isPresent()) {
                    assignTargetToField(field, returnObject, riskCategory.get());
                }
            }
        } else {
            final Class<?> typeOfClass = field.getType();
            Object targetObject = createNewInstance(typeOfClass);

            final String name = dataField.name();
            final Field fieldOfComposedObject = typeOfClass.getDeclaredField(name);
            processPrimitiveField(dataField, fieldOfComposedObject, targetObject, fileContent);

            assignTargetToField(field, returnObject, targetObject);
        }
    }

    private Object castToGivenType(Field field, String targetValue) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        final Class<?> classOfField = field.getType();
        if (classOfField.equals(BigDecimal.class)) {
            return BigDecimal.valueOf(Double.valueOf(targetValue));
        }
        final Constructor<?> constructor = classOfField.getConstructor(String.class);
        return constructor.newInstance(targetValue);
    }

    private void assignTargetToField(Field field, Object returnObject, Object targetValue) throws NoSuchFieldException, IllegalAccessException {
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

    private String processDataField(Field field, DataField dataField, String fileContent) {
        if (dataField.padding().isEmpty()) {
            return extract(fileContent, Integer.valueOf(dataField.startIndex()), dataField.length()).trim();
        }
        return stripStart(
                extract(fileContent, Integer.valueOf(dataField.startIndex()), dataField.length()),
                dataField.padding());
    }

    private String getStringOfFile(Path file) {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            return reader.readLine();
        } catch (IOException e) {
            throw new SecurityAccountOverviewReadException(e);
        }
    }

    private void checkIfConnector(Object object) throws Exception {
        if (Objects.isNull(object)) {
            throw new Exception("not possible");
        }

        Class<?> clazz = object.getClass();
        if (!clazz.isAnnotationPresent(Connector.class)) {
            throw new Exception("the class " + clazz.getSimpleName() + "is not annotated with JsonSerializable");
        }
    }

    private Object initializeObject(Object mapper, Object file) throws Exception {
        Class<?> clazz = mapper.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals("read")) { //TODO: make this generic
                method.setAccessible(true);
            }
            return method.invoke(mapper, file);
        }
        return null;

    }

    private String extract(String content, int startIndex, int length) {
        return content.substring(startIndex, startIndex + length);
    }

    private Object createNewInstance(Class<?> typeOfClass) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {

        Object targetObject = null;
        try {
            targetObject = typeOfClass.newInstance();
        } catch (Exception e) {
            System.out.println(e);
        }
        if (targetObject == null) {
            Field[] fields = typeOfClass.getDeclaredFields();
            List<Class> types = new ArrayList<>();
            List<Object> values = new ArrayList<>();
            for (Field fieldToGetClassFrom : fields) {
                types.add(fieldToGetClassFrom.getType());
                values.add(null);
            }

            Class[] classes = new Class[types.size()];
            Object[] objects = new Object[values.size()];
            final Constructor<?> constructor = typeOfClass.getConstructor(types.toArray(classes));
            return constructor.newInstance(objects);
        }
        return targetObject;
    }
}
