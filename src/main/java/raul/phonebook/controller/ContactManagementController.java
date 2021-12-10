package raul.phonebook.controller;

import com.turkraft.springfilter.boot.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raul.phonebook.dto.contact.ContactAdminDTO;
import raul.phonebook.dto.contact.CreateOrUpdateContactAdminDTO;
import raul.phonebook.model.contact.Contact;
import raul.phonebook.service.ContactManagementService;

import javax.validation.Valid;
import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/contact-management")
@RestController
public class ContactManagementController {

    private final ContactManagementService contactManagementService;

    @GetMapping("/{id}")
    ResponseEntity<ContactAdminDTO> getById(@PathVariable("id") Long id) throws Exception {
        var response = this.contactManagementService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    ResponseEntity<Page<ContactAdminDTO>> getPage(
            @Filter Specification<Contact> spec,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "500") int size) throws Exception {
        var response = this.contactManagementService.getPage(spec, PageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    ResponseEntity<ContactAdminDTO> createContact(@RequestBody @Valid CreateOrUpdateContactAdminDTO newContact) throws Exception {
        var response = this.contactManagementService.createContact(newContact);
        return ResponseEntity.created(URI.create("/contact-management/" + response.getId())).body(response);
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> updateContact(@PathVariable("id") Long id,
                                    @RequestBody @Valid CreateOrUpdateContactAdminDTO updateContact) throws Exception {
        this.contactManagementService.updateContact(id, updateContact);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteContact(@PathVariable("id") Long id) throws Exception {
        this.contactManagementService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/is-telephone-registered")
    ResponseEntity<Boolean> isTelephoneAlreadyRegistered(@RequestParam Long contactOwnerId,
                                                         @RequestParam String telephone) throws Exception {
        return ResponseEntity.ok(contactManagementService.isTelephoneAlreadyRegisteredByUser(contactOwnerId, telephone));
    }

}
