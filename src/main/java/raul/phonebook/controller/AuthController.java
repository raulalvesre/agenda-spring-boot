package raul.phonebook.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raul.phonebook.dto.auth.*;
import raul.phonebook.dto.user.CreateOrUpdateUserDTO;
import raul.phonebook.dto.user.UserDTO;
import raul.phonebook.service.AuthService;
import raul.phonebook.service.UserService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    ResponseEntity<UserDTO> register(@RequestBody  @Valid CreateOrUpdateUserDTO newUser) throws Exception {
        return ResponseEntity.ok(userService.createUser(newUser));
    }

    @PostMapping("/confirm-email")
    ResponseEntity<Void> confirmEmail(@RequestBody @Valid ConfirmEmailRequest confirmEmailReq) throws Exception {
        authService.confirmEmail(confirmEmailReq);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.getToken(loginRequest));
    }

    @PostMapping("/receive-password-recovery-token-on-email")
    ResponseEntity<Void> receivePasswordRecoveryEmail(@RequestBody SendRecoveryPasswordTokenOnEmailRequest req) throws Exception {
        authService.sendPasswordRecoveryTokenOnEmail(req);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/change-password")
    ResponseEntity<UserDTO> handlePasswordChange(@RequestBody @Valid ChangePasswordRequest req) throws Exception {
        authService.handlePasswordChange(req);
        return ResponseEntity.noContent().build();
    }

}
