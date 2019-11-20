package at.technikum.wien.mse.swe.connector;

import at.technikum.wien.mse.swe.exception.SecurityAccountOverviewReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

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

    private void updateValuesFromAnnotations(Object returnObject, Object connector, Path file) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = connector.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(DataField.class)) {
                DataField dataField = field.getAnnotation(DataField.class);
                final Alignment alignment = dataField.alignment();
                final int length = dataField.length();
                final String padding = dataField.padding();
                final String position = dataField.position();
                final String accountNumber = stripStart(
                        extract(getStringOfFile(file), Integer.valueOf(position), length),
                        padding);

                final Field fieldOnReturnObject = returnObject.getClass().getDeclaredField(field.getName());
                fieldOnReturnObject.setAccessible(true);
                fieldOnReturnObject.set(returnObject,accountNumber);
            }
        }
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
}
