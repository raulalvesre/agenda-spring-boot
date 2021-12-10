package raul.phonebook.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = { TelephoneValidator.class })
public @interface Telephone {
    public String message() default "Invalid telephone number for selected type";
    public Class<?>[] groups() default {};
    public Class<? extends Payload>[] payload() default {};
}
