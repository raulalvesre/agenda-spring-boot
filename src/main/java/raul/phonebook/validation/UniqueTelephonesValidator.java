package raul.phonebook.validation;

import raul.phonebook.dto.contact.telephone.CreateOrUpdateContactTelephoneDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class UniqueTelephonesValidator implements ConstraintValidator<UniqueTelephones, List<CreateOrUpdateContactTelephoneDTO>> {

    @Override
    public boolean isValid(List<CreateOrUpdateContactTelephoneDTO> valueToBeValidated, ConstraintValidatorContext context) {
        if (valueToBeValidated == null)
            return true;

        var uniqueTelephoneNumbers = valueToBeValidated.stream()
                .map(CreateOrUpdateContactTelephoneDTO::getTelephoneNumber)
                .distinct().count();

        return uniqueTelephoneNumbers == valueToBeValidated.size();
    }

}
