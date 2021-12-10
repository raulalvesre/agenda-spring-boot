package raul.phonebook.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import raul.phonebook.dto.auth.*;
import raul.phonebook.exception.NotFoundException;
import raul.phonebook.model.PasswordRecoveryToken;
import raul.phonebook.model.user.User;
import raul.phonebook.repository.ConfirmEmailTokenRepository;
import raul.phonebook.repository.PasswordRecoveryTokenRepository;
import raul.phonebook.repository.UserRepository;
import raul.phonebook.security.JwtTokenUtil;
import raul.phonebook.service.AuthService;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordRecoveryTokenRepository passwordRecoveryTokenRepository;
    private final ConfirmEmailTokenRepository confirmEmailTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender emailSender;

    @Override
    public LoginResponse getToken(LoginRequest loginRequest) {
        var username = loginRequest.getUsername();
        var password = loginRequest.getPassword();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            User user = userRepository.getByUsername(username);

            String token = jwtTokenUtil.createToken(user.getId(), user.getUsername(), user.getRole().getName());

            return new LoginResponse(token);
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username/password");
        }
    }

    @Override
    public void confirmEmail(ConfirmEmailRequest confirmEmailReq) {
        var dbToken = confirmEmailTokenRepository
                .findById(confirmEmailReq.getToken())
                .orElseThrow(() -> new NotFoundException("Non existent token"));

        var tokenDate = dbToken.getCreatedDate();
        if (tokenDate.isAfter(tokenDate.plus(2, ChronoUnit.HOURS)))
            throw new ResponseStatusException(HttpStatus.GONE, "Email confirmation token is expired");

        var user = dbToken.getOwner();
        user.setEnabled(true);

        userRepository.save(user);
    }

    @Override
    public void sendPasswordRecoveryTokenOnEmail(SendRecoveryPasswordTokenOnEmailRequest sendRecovEmailPasswdReq) throws Exception {
        var email = sendRecovEmailPasswdReq.getEmail();
        var user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new NotFoundException("No users with email [" + email +"]"));

        var changePasswordToken = UUID.randomUUID().toString();
        var passwordRecoveryToken = new PasswordRecoveryToken(changePasswordToken, user);

        passwordRecoveryTokenRepository.save(passwordRecoveryToken);

        var msg = emailSender.createMimeMessage();
        var msgHelper = new MimeMessageHelper(msg, true);
        var msgText = "Your password recovery token is: " + changePasswordToken;

        msgHelper.setTo(sendRecovEmailPasswdReq.getEmail());
        msgHelper.setSubject("Raul's Phonebook: Reset password token");
        msgHelper.setText(msgText, true);

        emailSender.send(msg);
    }

    @Override
    public void handlePasswordChange(ChangePasswordRequest changePasswordRequest) {
        var dbToken = passwordRecoveryTokenRepository
                .findById(changePasswordRequest.getToken())
                .orElseThrow(() -> new NotFoundException("Non existent token"));

        var tokenDate = dbToken.getCreatedDate();
        if (tokenDate.isAfter(tokenDate.plus(2, ChronoUnit.HOURS)))
            throw new ResponseStatusException(HttpStatus.GONE, "Password recovery token is expired");

        var user = dbToken.getOwner();
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));

        userRepository.save(user);
    }

}
