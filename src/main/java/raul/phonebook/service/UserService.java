package raul.phonebook.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import raul.phonebook.dto.user.CreateOrUpdateUserAdminDTO;
import raul.phonebook.dto.user.CreateOrUpdateUserDTO;
import raul.phonebook.dto.user.UserDTO;
import raul.phonebook.model.user.User;

public interface UserService {

    UserDTO getById(long userId) throws Exception;
    Page<UserDTO> getPage(Specification<User> spec, PageRequest pageRequest) throws Exception;
    UserDTO createUser(CreateOrUpdateUserDTO createOrUpdateUserDTO) throws Exception;
    UserDTO createUser(CreateOrUpdateUserAdminDTO createOrUpdateUserAdminDTO) throws Exception;
    void updateUser(long userId, CreateOrUpdateUserDTO createOrUpdateUserDTO) throws Exception;
    void updateUser(long userId, CreateOrUpdateUserAdminDTO createOrUpdateUserAdminDTO) throws Exception;
    void deleteUser(long userId) throws Exception;

    boolean isUsernameAlreadyRegistered(String username);
    boolean isEmailAlreadyRegistered(String email);

}
