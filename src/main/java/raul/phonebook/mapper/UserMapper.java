package raul.phonebook.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import raul.phonebook.dto.user.CreateOrUpdateUserAdminDTO;
import raul.phonebook.dto.user.UserDTO;
import raul.phonebook.dto.user.CreateOrUpdateUserDTO;
import raul.phonebook.exception.NotFoundException;
import raul.phonebook.model.types.Role;
import raul.phonebook.model.user.User;
import raul.phonebook.repository.RoleRepository;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

    @Autowired
    protected RoleRepository roleRepository;
    @Autowired
    protected PasswordEncoder passwordEncoder;

    //region USER MAPPERS
    @Mapping(target = "password", qualifiedByName = "encodePassword")
    @Mapping(target = "role", expression = "java(getUserRoleFromDb())")
    @Mapping(target = "enabled", constant = "false")
    public abstract User toUser(CreateOrUpdateUserDTO createOrUpdateUserDTO) throws Exception;

    @InheritConfiguration
    public abstract void fromUserRequest(CreateOrUpdateUserDTO createOrUpdateUserDTO, @MappingTarget User user);
    //endregion

    //region ADM MAPPERS
    @Mapping(target = "password", qualifiedByName = "encodePassword")
    @Mapping(target = "role", qualifiedByName = "getRoleFromDb")
    @Mapping(target = "enabled", constant = "true")
    public abstract User toUser(CreateOrUpdateUserAdminDTO createOrUpdateUserAdminDTO) throws Exception;

    @InheritConfiguration
    public abstract void fromUserAdmRequest(CreateOrUpdateUserAdminDTO createOrUpdateUserAdminDTO, @MappingTarget User user);
    //endregion

    @Mapping(target = "role", source = "role.id")
    public abstract UserDTO toUserResponse(User user);

    public abstract List<UserDTO> toUserResponseList(List<User> users);

    //region NAMED METHODS
    @Named("encodePassword")
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Named("getRoleFromDb")
    public Role getRoleFromDb(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new NotFoundException("Role with id=" + roleId + " does not exist"));
    }

    @Named("getUserRoleFromDb")
    public Role getUserRoleFromDb() {
        return roleRepository.getById(2L);
    }
    //endregion

}
