package raul.phonebook.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import raul.phonebook.dto.user.CreateOrUpdateUserDTO;
import raul.phonebook.dto.user.UserDTO;
import raul.phonebook.security.MyUserDetails;
import raul.phonebook.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {

    private final UserService userService;

    @GetMapping
    ResponseEntity<UserDTO> getMe(Authentication authentication) throws Exception {
        var requesterId = ((MyUserDetails) authentication.getPrincipal()).getId();
        var response = this.userService.getById(requesterId);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    ResponseEntity<Void> updateMe(@RequestBody @Valid CreateOrUpdateUserDTO updatedUser,
                                  Authentication authentication) throws Exception {
        var requesterId = ((MyUserDetails) authentication.getPrincipal()).getId();
        this.userService.updateUser(requesterId, updatedUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    ResponseEntity<Void> deleteMe(Authentication authentication) throws Exception {
        var requesterId = ((MyUserDetails) authentication.getPrincipal()).getId();
        this.userService.deleteUser(requesterId);
        return ResponseEntity.noContent().build();
    }

}

