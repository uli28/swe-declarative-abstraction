package at.technikum.wien.mse.swe.creators.impl;

import at.technikum.wien.mse.swe.creators.ConnectorFactory;
import at.technikum.wien.mse.swe.exception.SecurityAccountOverviewReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Default implementation of Connector Factory
 *
 * @param <T> the target model
 * @author Ulrich Gram
 */
public class DefaultConnectorFactory<T> implements ConnectorFactory {
    private Class mapperClass;
    private Class targetClass;

    public DefaultConnectorFactory(Class mapper, Class target) {
        this.mapperClass = mapper;
        this.targetClass = target;
    }

    @Override
    public T read(Path file) {
        return new DefaultModelCreator<T>(mapperClass, targetClass).convert(getStringOfFile(file));
    }

    private String getStringOfFile(Path file) {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            return reader.readLine();
        } catch (IOException e) {
            throw new SecurityAccountOverviewReadException(e);
        }
    }
}
