package raul.phonebook.controller;

import com.turkraft.springfilter.boot.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raul.phonebook.dto.contact.ContactDTO;
import raul.phonebook.dto.contact.CreateOrUpdateContactDTO;
import raul.phonebook.model.contact.Contact;
import raul.phonebook.service.ContactService;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @GetMapping("/{id}")
    ResponseEntity<ContactDTO> getById(@PathVariable("id") Long id,
                                       Principal principal) throws Exception {
        var response = this.contactService.getById(principal.getName(), id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    ResponseEntity<Page<ContactDTO>> getPage(
            @Filter Specification<Contact> spec,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "500") int size,
            Principal principal) throws Exception {
        var response = this.contactService.getPage(principal.getName(), spec, PageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }

    @PostMapping
    ResponseEntity<ContactDTO> createContact(@RequestBody @Valid CreateOrUpdateContactDTO newContact,
                                             Principal principal) throws Exception {
        var response = this.contactService.createContact(principal.getName(), newContact);
        return ResponseEntity.created(URI.create("/contact-management/" + response.getId())).body(response);
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> updateContact(@PathVariable("id") Long id,
                                       @RequestBody @Valid CreateOrUpdateContactDTO updateContact,
                                       Principal principal) throws Exception {
        this.contactService.updateContact(principal.getName(), id, updateContact);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteContact(@PathVariable("id") Long id,
                                       Principal principal) throws Exception {
        this.contactService.deleteContact(principal.getName(), id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/is-telephone-registered")
    ResponseEntity<Boolean> isTelephoneAlreadyRegistered(@RequestParam String telephone,
                                                         Principal principal) throws Exception {
        return ResponseEntity.ok(contactService.isTelephoneAlreadyRegisteredByUser(principal.getName(), telephone));
    }


}
