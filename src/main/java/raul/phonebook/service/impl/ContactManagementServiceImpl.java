package raul.phonebook.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import raul.phonebook.dto.contact.ContactAdminDTO;
import raul.phonebook.dto.contact.CreateOrUpdateContactAdminDTO;
import raul.phonebook.dto.contact.telephone.CreateOrUpdateContactTelephoneDTO;
import raul.phonebook.exception.UnprocessableEntityException;
import raul.phonebook.exception.NotFoundException;
import raul.phonebook.mapper.ContactMapper;
import raul.phonebook.model.contact.Contact;
import raul.phonebook.repository.ContactRepository;
import raul.phonebook.service.ContactManagementService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactManagementServiceImpl implements ContactManagementService {

    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;

    @Override
    public ContactAdminDTO getById(long contactId) throws Exception {
        var contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new NotFoundException("Contact not found"));

        return contactMapper.toContactAdmResp(contact);
    }

    @Override
    public Page<ContactAdminDTO> getPage(Specification<Contact> spec,
                                         PageRequest pageRequest) throws Exception {
        pageRequest.withSort(Sort.Direction.DESC, "id");

        return contactRepository
                .findAll(spec, pageRequest)
                .map(contactMapper::toContactAdmResp);
    }

    @Override
    public ContactAdminDTO createContact(CreateOrUpdateContactAdminDTO contactAdmReq) throws Exception {
        var alreadyRegisteredTelephoneMessages = getAlreadyRegisteredTelephoneMessagesByOwnerId(contactAdmReq.getOwner(), contactAdmReq.getTelephones());
        if (!alreadyRegisteredTelephoneMessages.isEmpty())
            throw new UnprocessableEntityException(alreadyRegisteredTelephoneMessages);

        var contact = contactMapper.toContact(contactAdmReq);
        contactRepository.save(contact);

        return contactMapper.toContactAdmResp(contact);
    }

    private List<String> getAlreadyRegisteredTelephoneMessagesByOwnerId(
            Long ownerId,
            List<CreateOrUpdateContactTelephoneDTO> tels) {
        var conflicts = new ArrayList<String>();

        for (var tel : tels) {
            var currentTelNumber = tel.getTelephoneNumber();
            if (isTelephoneAlreadyRegisteredByUser(ownerId, currentTelNumber)) {
                String msg = String.format("Telephone [%s] is already registered", currentTelNumber);
                conflicts.add(msg);
            }
        }

        return conflicts;
    }

    @Override
    public void updateContact(long contactId, CreateOrUpdateContactAdminDTO contactAdmReq) throws Exception {
        var alreadyRegisteredTelephoneMessages = getAlreadyRegisteredTelephoneMessagesExcludingExistingContact(contactId, contactAdmReq.getTelephones());
        if (!alreadyRegisteredTelephoneMessages.isEmpty())
            throw new UnprocessableEntityException(alreadyRegisteredTelephoneMessages);

        var contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new NotFoundException("Contact with id=" + contactId + " not found"));

        contactMapper.fromContactAdmReq(contactAdmReq, contact);

        contactRepository.save(contact);
    }

    private List<String> getAlreadyRegisteredTelephoneMessagesExcludingExistingContact(
            Long existingContactId,
            List<CreateOrUpdateContactTelephoneDTO> tels) {
        var conflicts = new ArrayList<String>();

        for (var tel : tels) {
            var currentTelNumber = tel.getTelephoneNumber();
            if (isTelephoneAlreadyRegisteredExcludingExistingContact(existingContactId, currentTelNumber)) {
                String msg = String.format("Telephone [%s] is already registered", currentTelNumber);
                conflicts.add(msg);
            }
        }

        return conflicts;
    }

    @Override
    public void deleteContact(long contactId) throws Exception {
        var contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new NotFoundException("Contact with id=" + contactId + " not found"));

        contactRepository.delete(contact);
    }

    @Override
    public boolean isTelephoneAlreadyRegisteredByUser(Long ownerId, String telephone) {
        return contactRepository.existsByOwnerIdAndTelephonesTelephoneFormatted(ownerId, telephone);
    }

    @Override
    public boolean isTelephoneAlreadyRegisteredExcludingExistingContact(Long existingContactId, String telephone) {
        return contactRepository.existsByIdNotAndTelephonesTelephoneFormatted(existingContactId, telephone);
    }

}
