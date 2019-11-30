package at.technikum.wien.mse.swe.converter.impl;

import at.technikum.wien.mse.swe.converter.MapperConverter;
import at.technikum.wien.mse.swe.mapper.ComplexField;
import at.technikum.wien.mse.swe.mapper.DataField;
import at.technikum.wien.mse.swe.mapper.Mapper;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.complexfield.ComplexFieldProcessorStrategy;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.complexfield.DefaultComplexFieldProcessorStrategy;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield.DefaultFieldProcessor;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield.EnumFieldProcessor;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield.PrimitiveFieldProcessor;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.simplefield.DefaultSimpleFieldProcessorFactory;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.simplefield.SimpleFieldProcessorFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class DefaultMapperConverter<T> implements MapperConverter {
    private Class mapper;
    private Class<T> target;
    private SimpleFieldProcessorFactory simpleFieldProcessorFactory;
    private ComplexFieldProcessorStrategy complexFieldProcessorStrategy;

    DefaultMapperConverter(Class mapper, Class<T> target) {
        this.mapper = mapper;
        this.target = target;
        this.simpleFieldProcessorFactory = new DefaultSimpleFieldProcessorFactory(
                Arrays.asList(new PrimitiveFieldProcessor(),
                        new EnumFieldProcessor(), new DefaultFieldProcessor()));
        this.complexFieldProcessorStrategy = new DefaultComplexFieldProcessorStrategy();
    }

    @Override
    public T convert(String fileContent) {
        T targetObject = null;
        try {
            if (isMapperAnnotationPresent()) {
                targetObject = initializeObject(target);
                if (targetObject != null) {
                    mapValues(targetObject, mapper, fileContent);
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

    private void mapValues(Object returnObject, Class mapper, String fileContent) throws NoSuchFieldException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        for (Field field : mapper.getDeclaredFields()) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(DataField.class)) {
                final DataField dataField = field.getAnnotation(DataField.class);
                this.simpleFieldProcessorFactory.process(dataField, field, returnObject, fileContent);

            } else if (field.isAnnotationPresent(ComplexField.class)) {
                complexFieldProcessorStrategy.process(field, returnObject, fileContent);
            }
        }
    }
}
