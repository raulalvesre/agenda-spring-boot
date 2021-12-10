package raul.phonebook.service;

import raul.phonebook.dto.auth.*;

public interface AuthService {
    LoginResponse getToken(LoginRequest loginRequest);
    void confirmEmail(ConfirmEmailRequest confirmEmailReq);
    void sendPasswordRecoveryTokenOnEmail(SendRecoveryPasswordTokenOnEmailRequest sendRecovOnEmailPasswdReq) throws Exception;
    void handlePasswordChange(ChangePasswordRequest newPassword);
}
