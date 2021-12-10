package raul.phonebook.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import raul.phonebook.dto.contact.ContactAdminDTO;
import raul.phonebook.dto.contact.CreateOrUpdateContactAdminDTO;
import raul.phonebook.model.contact.Contact;

public interface ContactManagementService {

    ContactAdminDTO getById(long contactId) throws Exception;
    Page<ContactAdminDTO> getPage(Specification<Contact> spec, PageRequest pageRequest) throws Exception;
    ContactAdminDTO createContact(CreateOrUpdateContactAdminDTO createOrUpdateContactAdminDTO) throws Exception;
    void updateContact(long contactId, CreateOrUpdateContactAdminDTO createOrUpdateContactAdminDTO) throws Exception;
    void deleteContact(long contactId) throws Exception;

    boolean isTelephoneAlreadyRegisteredByUser(Long ownerId, String telephone);
    boolean isTelephoneAlreadyRegisteredExcludingExistingContact(Long existingContactId, String telephone);

}
