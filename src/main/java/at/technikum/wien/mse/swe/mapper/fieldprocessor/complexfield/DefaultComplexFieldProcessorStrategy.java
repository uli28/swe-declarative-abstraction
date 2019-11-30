package at.technikum.wien.mse.swe.mapper.fieldprocessor.complexfield;

import at.technikum.wien.mse.swe.mapper.ComplexField;
import at.technikum.wien.mse.swe.mapper.DataField;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.FieldProcessorUtil;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield.DefaultFieldProcessor;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield.EnumFieldProcessor;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield.PrimitiveFieldProcessor;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield.extractor.DefaultValueExtractor;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield.extractor.ValueExtractor;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.simplefield.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class DefaultComplexFieldProcessorStrategy implements ComplexFieldProcessorStrategy {
    private SimpleFieldProcessorFactory simpleFieldProcessorFactory;

    public DefaultComplexFieldProcessorStrategy() {
        this.simpleFieldProcessorFactory = new DefaultSimpleFieldProcessorFactory(
                Arrays.asList(new PrimitiveFieldProcessor(),
                        new EnumFieldProcessor(), new DefaultFieldProcessor()));
    }

    @Override
    public void process(Field field, Object returnObject, String fileContent) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        final ComplexField complexField = field.getAnnotation(ComplexField.class);
        final DataField[] primitiveFields = complexField.fieldMapper();
        final Class<?> typeOfClass = field.getType();
        final Object targetObject = FieldProcessorUtil.createNewInstance(typeOfClass);

        for (final DataField dataField : primitiveFields) {
            final String name = dataField.name();
            final Field fieldOfComposedObject = typeOfClass.getDeclaredField(name);
            simpleFieldProcessorFactory.process(dataField, fieldOfComposedObject, targetObject, fileContent);
        }
        FieldProcessorUtil.assignTargetToField(field, returnObject, targetObject);
    }
}
