package at.technikum.wien.mse.swe.mapper.fieldprocessor.datafield;

import at.technikum.wien.mse.swe.mapper.DataField;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.FieldProcessorUtil;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.simplefield.DefaultSimpleFieldProcessorFactory;
import at.technikum.wien.mse.swe.mapper.fieldprocessor.simplefield.SimpleFieldProcessorFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class DefaultFieldProcessor implements FieldProcessorStrategy {

    private SimpleFieldProcessorFactory simpleFieldProcessorFactory;

    public DefaultFieldProcessor() {
        simpleFieldProcessorFactory = new DefaultSimpleFieldProcessorFactory(
                Arrays.asList(new PrimitiveFieldProcessor(), new EnumFieldProcessor())
        );
    }

    @Override
    public void process(DataField dataField, Field field, Object returnObject, String fileContent, Object targetValue) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {

        final Class<?> typeOfClass = field.getType();
        final Object targetObject = FieldProcessorUtil.createNewInstance(typeOfClass);

        final String name = dataField.name();
        final Field fieldOfComposedObject = typeOfClass.getDeclaredField(name);
        processPrimitiveField(dataField, fieldOfComposedObject, targetObject, fileContent, (String) targetValue);

        FieldProcessorUtil.assignTargetToField(field, returnObject, targetObject);
    }

    private void processPrimitiveField(DataField dataField, Field fieldOfComposedObject, Object returnObject, String fileContent, String targetObject)
            throws InvocationTargetException, NoSuchMethodException, NoSuchFieldException, InstantiationException, IllegalAccessException {
        simpleFieldProcessorFactory.process(dataField, fieldOfComposedObject, returnObject, fileContent);
    }


    @Override
    public boolean canHandle(Field field) {
        return true;
    }
}
