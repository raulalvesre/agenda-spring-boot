package raul.phonebook.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import raul.phonebook.dto.contact.ContactDTO;
import raul.phonebook.dto.contact.CreateOrUpdateContactDTO;
import raul.phonebook.model.contact.Contact;

public interface ContactService {

    ContactDTO getById(String requesterUsername, long contactId) throws Exception;
    Page<ContactDTO> getPage(String requesterUsername, Specification<Contact> spec, PageRequest pageRequest) throws Exception;
    ContactDTO createContact(String requesterUsername, CreateOrUpdateContactDTO createOrUpdateContactDTO) throws Exception;
    void updateContact(String requesterUsername, long contactId, CreateOrUpdateContactDTO createOrUpdateContactDTO) throws Exception;
    void deleteContact(String requesterUsername, long contactId) throws Exception;

    boolean isTelephoneAlreadyRegisteredByUser(String requesterUsername, String telephone);
    boolean isTelephoneAlreadyRegisteredExcludingExistingContact(Long existingContactId, String telephone);
}
