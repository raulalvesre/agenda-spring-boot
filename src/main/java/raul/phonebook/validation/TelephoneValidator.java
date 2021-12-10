package raul.phonebook.validation;

import raul.phonebook.dto.contact.telephone.CreateOrUpdateContactTelephoneDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TelephoneValidator implements ConstraintValidator<Telephone, CreateOrUpdateContactTelephoneDTO> {

    @Override
    public boolean isValid(CreateOrUpdateContactTelephoneDTO telephoneToBeValidated, ConstraintValidatorContext context) {
        if (telephoneToBeValidated.getTelephoneNumber() == null
                || telephoneToBeValidated.getType() == null)
            return true; //@NotNull will take care of it

        Pattern telPattern;

        if (telephoneToBeValidated.getType().equals(1L)) {
            telPattern = Pattern.compile("^\\((?:[14689][1-9]|2[12478]|3[1234578]|5[1345]|7[134579])\\) 9[1-9][0-9]{3}\\-[0-9]{4}$");
        } else if (telephoneToBeValidated.getType().equals(2L)) {
            telPattern = Pattern.compile("^\\((?:[14689][1-9]|2[12478]|3[1234578]|5[1345]|7[134579])\\) [2-8][0-9]{3}\\-[0-9]{4}$");
        } else if (telephoneToBeValidated.getType().equals(3L)) {
            telPattern = Pattern.compile("^\\((?:[14689][1-9]|2[12478]|3[1234578]|5[1345]|7[134579])\\) (?:[2-8]|9[1-9])[0-9]{3}\\-[0-9]{4}$");
        } else {
            return true; //mapper will take care of it
        }

        Matcher matcher = telPattern.matcher(telephoneToBeValidated.getTelephoneNumber());
        return matcher.matches();
    }
}