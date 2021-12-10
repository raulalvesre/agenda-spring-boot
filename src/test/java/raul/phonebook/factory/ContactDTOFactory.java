package raul.phonebook.factory;

import raul.phonebook.dto.contact.CreateOrUpdateContactAdminDTO;
import raul.phonebook.dto.contact.CreateOrUpdateContactDTO;

public class ContactDTOFactory {

    public static CreateOrUpdateContactDTO buildValidCreateOrUpdateContactDTO() {
        var telephones = ContactTelephoneDTOFactory.buildValidListOfCreateOrUpdateContactTelephoneDTO();

        return new CreateOrUpdateContactDTO(
                "contact",
                telephones);
    }

    public static CreateOrUpdateContactAdminDTO buildValidCreateOrUpdateContactAdminDTO() {
        var telephones = ContactTelephoneDTOFactory.buildValidListOfCreateOrUpdateContactTelephoneDTO();

        return new CreateOrUpdateContactAdminDTO(
                "contact",
                telephones,
                1L);
    }

}
