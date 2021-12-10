package raul.phonebook.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import raul.phonebook.dto.contact.ContactAdminDTO;
import raul.phonebook.dto.contact.CreateOrUpdateContactAdminDTO;
import raul.phonebook.dto.contact.CreateOrUpdateContactDTO;
import raul.phonebook.dto.contact.ContactDTO;
import raul.phonebook.exception.NotFoundException;
import raul.phonebook.model.contact.Contact;
import raul.phonebook.model.user.User;
import raul.phonebook.repository.UserRepository;

@Mapper(
        componentModel = "spring",
        uses = ContactTelephoneMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ContactMapper {

    @Autowired
    private UserRepository userRepository;

    //region USER MAPPERS
    public abstract Contact toContact(CreateOrUpdateContactDTO createOrUpdateContactDTO);
    public abstract ContactDTO toContactResp(Contact contact);

    @InheritConfiguration
    public abstract void fromContactReq(CreateOrUpdateContactDTO createOrUpdateContactDTO, @MappingTarget Contact contact);
    //endregion

    //region ADM MAPPERS
    @Mapping(target = "owner", qualifiedByName = "getUserFromDb")
    public abstract Contact toContact(CreateOrUpdateContactAdminDTO createOrUpdateContactAdminDTO) throws Exception;
    
    @Mapping(target = "ownerId", source = "owner.id")
    public abstract ContactAdminDTO toContactAdmResp(Contact contact);

    @InheritConfiguration
    public abstract void fromContactAdmReq(CreateOrUpdateContactAdminDTO createOrUpdateContactAdminDTO, @MappingTarget Contact contact);

    //endregion

    //region NAMED METHODS
    @Named("getUserFromDb")
    public User getUserFromDb(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " does not exist"));
    }
    //endregion

}
