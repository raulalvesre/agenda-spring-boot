package raul.phonebook.factory;

import raul.phonebook.dto.contact.telephone.CreateOrUpdateContactTelephoneDTO;

import java.util.List;

public class ContactTelephoneDTOFactory {

    public static List<CreateOrUpdateContactTelephoneDTO> buildValidListOfCreateOrUpdateContactTelephoneDTO() {
        return List.of(
                new CreateOrUpdateContactTelephoneDTO(1L, "tel1", "(11) 97645-4630"),
                new CreateOrUpdateContactTelephoneDTO(2L, "tel2", "(11) 4002-8922"));
    }

}
