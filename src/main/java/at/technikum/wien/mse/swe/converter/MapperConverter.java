package at.technikum.wien.mse.swe.converter;

public interface MapperConverter<T> {

    T convert(String fileContent) throws IllegalAccessException, InstantiationException;
}
