package raul.phonebook.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import raul.phonebook.dto.contact.CreateOrUpdateContactDTO;
import raul.phonebook.dto.contact.ContactDTO;
import raul.phonebook.dto.contact.telephone.CreateOrUpdateContactTelephoneDTO;
import raul.phonebook.exception.UnprocessableEntityException;
import raul.phonebook.exception.NotFoundException;
import raul.phonebook.mapper.ContactMapper;
import raul.phonebook.model.contact.Contact;
import raul.phonebook.repository.ContactRepository;
import raul.phonebook.repository.UserRepository;
import raul.phonebook.service.ContactService;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final ContactMapper contactMapper;

    @Override
    public ContactDTO getById(String requesterUsername, long contactId) throws Exception {
        var contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Contact not found."));

        if (!contact.getOwner().getUsername().equals(requesterUsername))
            throw new ResponseStatusException(FORBIDDEN, "This contact is not yours.");

        return contactMapper.toContactResp(contact);
    }

    @Override
    public Page<ContactDTO> getPage(String requesterUsername,
                                    Specification<Contact> spec,
                                    PageRequest pageRequest) throws Exception {
        pageRequest.withSort(Sort.Direction.DESC, "id");

        return contactRepository
                .findAllByOwnerUsername(requesterUsername, spec, pageRequest)
                .map(contactMapper::toContactResp);
    }

    @Override
    public ContactDTO createContact(String requesterUsername, CreateOrUpdateContactDTO createOrUpdateContactDTO) throws Exception {
        var alreadyRegisteredTelephoneMessages = getAlreadyRegisteredTelephoneMessagesByOwnerUsername(requesterUsername, createOrUpdateContactDTO.getTelephones());
        if (!alreadyRegisteredTelephoneMessages.isEmpty())
            throw new UnprocessableEntityException(alreadyRegisteredTelephoneMessages);

        var owner = userRepository.getByUsername(requesterUsername);

        var contact = contactMapper.toContact(createOrUpdateContactDTO);
        contact.setOwner(owner);
        contactRepository.save(contact);

        return contactMapper.toContactResp(contact);
    }

    private List<String> getAlreadyRegisteredTelephoneMessagesByOwnerUsername(
                                        String ownerUsername,
                                        List<CreateOrUpdateContactTelephoneDTO> tels) {
        var alreadyRegisteredTelephoneNumbers = new ArrayList<String>();

        for (var tel: tels) {
            var currentTelNumber = tel.getTelephoneNumber();
            if (isTelephoneAlreadyRegisteredByUser(ownerUsername, currentTelNumber)) {
                String msg = String.format("Telephone [%s] is already registered", currentTelNumber);
                alreadyRegisteredTelephoneNumbers.add(msg);
            }
        }

        return alreadyRegisteredTelephoneNumbers;
    }

    @Override
    public void updateContact(String requesterUsername, long contactId, CreateOrUpdateContactDTO createOrUpdateContactDTO) throws Exception {
        var alreadyRegisteredTelephoneMessages = getAlreadyRegisteredTelephoneMessagesExcludingExistingContact(contactId, createOrUpdateContactDTO.getTelephones());
        if (!alreadyRegisteredTelephoneMessages.isEmpty())
            throw new UnprocessableEntityException(alreadyRegisteredTelephoneMessages);

        var contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new NotFoundException("Contact with id=" + contactId + " not found"));

        if (!contact.getOwner().getUsername().equals(requesterUsername))
            throw new ResponseStatusException(FORBIDDEN, "This contact is not yours.");

        contactMapper.fromContactReq(createOrUpdateContactDTO, contact);

        contactRepository.save(contact);
    }

    private List<String> getAlreadyRegisteredTelephoneMessagesExcludingExistingContact(
            Long existingContactId,
            List<CreateOrUpdateContactTelephoneDTO> tels) {
        var alreadyRegisteredTelephoneNumbers = new ArrayList<String>();

        for (var tel: tels) {
            var currentTelNumber = tel.getTelephoneNumber();
            if (isTelephoneAlreadyRegisteredExcludingExistingContact(existingContactId, currentTelNumber)) {
                String msg = String.format("Telephone [%s] is already registered", currentTelNumber);
                alreadyRegisteredTelephoneNumbers.add(msg);
            }
        }

        return alreadyRegisteredTelephoneNumbers;
    }

    @Override
    public void deleteContact(String requesterUsername, long contactId) throws Exception {
        var contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new NotFoundException("Contact with id=" + contactId + " not found"));

        if (!contact.getOwner().getUsername().equals(requesterUsername))
            throw new ResponseStatusException(FORBIDDEN, "This contact is not yours.");

        contactRepository.delete(contact);
    }

    @Override
    public boolean isTelephoneAlreadyRegisteredByUser(String requesterUsername, String telephone) {
        return contactRepository.existsByOwnerUsernameAndTelephonesTelephoneFormatted(requesterUsername, telephone);
    }

    @Override
    public boolean isTelephoneAlreadyRegisteredExcludingExistingContact(Long existingContactId, String telephone) {
        return contactRepository.existsByIdNotAndTelephonesTelephoneFormatted(existingContactId, telephone);
    }

}
