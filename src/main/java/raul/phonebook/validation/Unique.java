package raul.phonebook.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = UniquenessValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
public @interface Unique {

    String message() default "{raul.phonebook.validation.unique}";

    Class<?>[] groups() default {} ;

    Class<? extends Payload>[] payload() default {};

    Class<?> entity();

    String field();
}
