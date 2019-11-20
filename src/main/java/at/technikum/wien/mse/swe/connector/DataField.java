package at.technikum.wien.mse.swe.connector;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataField {
    String BLANK_STRING = " ";

    String position();
    String padding() default BLANK_STRING;
    int length();
    Alignment alignment();
}
