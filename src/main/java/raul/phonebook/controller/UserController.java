package raul.phonebook.controller;

import com.turkraft.springfilter.boot.Filter;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raul.phonebook.dto.user.CreateOrUpdateUserAdminDTO;
import raul.phonebook.dto.user.UserDTO;
import raul.phonebook.model.user.User;
import raul.phonebook.service.UserService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{id}")
    ResponseEntity<UserDTO> userById(@PathVariable("id") Long id) throws Exception {
        var response = this.userService.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    ResponseEntity<Page<UserDTO>> getPage(
            @Filter Specification<User> spec,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "500") int size) throws Exception {
        var response = this.userService.getPage(spec, PageRequest.of(page, size));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users")
    ResponseEntity<UserDTO> createUser(@RequestBody @Valid CreateOrUpdateUserAdminDTO newUser) throws Exception {
        var response = this.userService.createUser(newUser);
        return ResponseEntity.created(URI.create("/users/" + response.getId())).body(response);
    }

    @PutMapping("/users/{id}")
    ResponseEntity<Void> updateUser(@PathVariable("id") Long id,
                                    @RequestBody @Valid CreateOrUpdateUserAdminDTO updateUser) throws Exception {
        this.userService.updateUser(id, updateUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable("id") Long id) throws Exception {
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/is-username-registered")
    ResponseEntity<Boolean> isUsernameAlreadyRegistered(String username) throws Exception {
        return ResponseEntity.ok(userService.isUsernameAlreadyRegistered(username));
    }

    @GetMapping("/users/is-email-registered")
    ResponseEntity<Boolean> isEmailAlreadyRegistered(String email) throws Exception {
        return ResponseEntity.ok(userService.isEmailAlreadyRegistered(email));
    }

}
