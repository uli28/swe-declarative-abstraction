package at.technikum.wien.mse.swe.converter.impl;

import at.technikum.wien.mse.swe.converter.ConnectorFactory;
import at.technikum.wien.mse.swe.exception.SecurityAccountOverviewReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DefaultConnectorFactory<T> implements ConnectorFactory {
    private Class mapperClass;
    private Class targetClass;

    public DefaultConnectorFactory(Class mapper, Class target) {
        this.mapperClass = mapper;
        this.targetClass = target;
    }

    @Override
    public T read(Path file) {
        T returnObject = null;
        try {
            return new DefaultMapperConverter<T>(mapperClass,targetClass).convert(getStringOfFile(file));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return returnObject;
    }

    private String getStringOfFile(Path file) {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            return reader.readLine();
        } catch (IOException e) {
            throw new SecurityAccountOverviewReadException(e);
        }
    }
}
