package raul.phonebook.factory;

import raul.phonebook.dto.user.CreateOrUpdateUserAdminDTO;
import raul.phonebook.dto.user.CreateOrUpdateUserDTO;

public class UserDTOFactory {

    public static CreateOrUpdateUserDTO buildValidCreateOrUpdateUserDTO() {
        return new CreateOrUpdateUserDTO(
                "name",
                "username",
                "email@gmail.com",
                "passwordd1"
                );
    }

    public static CreateOrUpdateUserAdminDTO buildValidCreateOrUpdateUserAdminDTO() {
        return new CreateOrUpdateUserAdminDTO(
                "name",
                "username",
                "email@gmail.com",
                "passwordd1",
                1L);
    }
}
