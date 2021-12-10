package raul.phonebook.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = { UniqueTelephonesValidator.class })
public @interface UniqueTelephones {
    public String message() default "All telephones must be unique";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
