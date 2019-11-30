package at.technikum.wien.mse.swe.converter;

import java.nio.file.Path;

public interface ConnectorFactory<T> {

    T read(Path file);
}
