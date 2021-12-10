package raul.phonebook.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import raul.phonebook.dto.user.CreateOrUpdateUserAdminDTO;
import raul.phonebook.dto.user.CreateOrUpdateUserDTO;
import raul.phonebook.dto.user.UserDTO;
import raul.phonebook.exception.UnprocessableEntityException;
import raul.phonebook.exception.NotFoundException;
import raul.phonebook.mapper.UserMapper;
import raul.phonebook.model.ConfirmEmailToken;
import raul.phonebook.model.user.User;
import raul.phonebook.repository.ConfirmEmailTokenRepository;
import raul.phonebook.repository.UserRepository;
import raul.phonebook.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JavaMailSender emailSender;
    private final ConfirmEmailTokenRepository confirmEmailTokenRepository;

    public UserDTO getById(long userId) throws Exception {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "User not found"));

        return userMapper.toUserResponse(user);
    }

    @Override
    public Page<UserDTO> getPage(Specification<User> spec,
                                 PageRequest pageRequest) throws Exception {
        pageRequest.withSort(Sort.Direction.DESC, "id");

        return userRepository
                .findAll(spec, pageRequest)
                .map(userMapper::toUserResponse);
    }

    @Override
    public UserDTO createUser(CreateOrUpdateUserDTO createOrUpdateUserDTO) throws Exception {
        var unprocessableReasons = getUnprocessableReasons(createOrUpdateUserDTO.getUsername(), createOrUpdateUserDTO.getEmail());
        if (!unprocessableReasons.isEmpty())
            throw new UnprocessableEntityException(unprocessableReasons);

        var user = userMapper.toUser(createOrUpdateUserDTO);
        userRepository.save(user);

        sendEmailConfirmationTokenOnEmail(user.getEmail());

        return userMapper.toUserResponse(user);
    }

    private void sendEmailConfirmationTokenOnEmail(String userEmail) throws Exception {
        var user = userRepository
                .getByEmail(userEmail);

        var confirmEmailToken = UUID.randomUUID().toString();
        var passwordRecoveryToken = new ConfirmEmailToken(confirmEmailToken, user);

        confirmEmailTokenRepository.save(passwordRecoveryToken);

        var msg = emailSender.createMimeMessage();
        var msgHelper = new MimeMessageHelper(msg, true);
        var msgText = "Your confirmation token is: " + confirmEmailToken;

        msgHelper.setTo(userEmail);
        msgHelper.setSubject("Raul's Phonebook: Email Confirmation Token");
        msgHelper.setText(msgText, true);

        emailSender.send(msg);
    }

    public UserDTO createUser(CreateOrUpdateUserAdminDTO createOrUpdateUserAdminDTO) throws Exception {
        var unprocessableReasons = getUnprocessableReasons(createOrUpdateUserAdminDTO.getUsername(), createOrUpdateUserAdminDTO.getEmail());
        if (!unprocessableReasons.isEmpty())
            throw new UnprocessableEntityException(unprocessableReasons);

        var user = userMapper.toUser(createOrUpdateUserAdminDTO);
        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    private List<String> getUnprocessableReasons(String username, String email) {
        var unprocessableReasons = new ArrayList<String>();

        if (isUsernameAlreadyRegistered(username)) {
            String msg = String.format("Username [%s] is already registered", username);
            unprocessableReasons.add(msg);
        }

        if (isEmailAlreadyRegistered(email)) {
            String msg = String.format("Email [%s] is already registered", email);
            unprocessableReasons.add(msg);
        }

        return unprocessableReasons;
    }

    public void updateUser(long userId, CreateOrUpdateUserDTO createOrUpdateUserDTO) throws Exception {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not with id=" + userId + " not found"));

        var unprocessableReasons = getUnprocessableReasonsExcludingExistingUser(createOrUpdateUserDTO.getUsername(), createOrUpdateUserDTO.getEmail(), userId);
        if (!unprocessableReasons.isEmpty())
            throw new UnprocessableEntityException(unprocessableReasons);

        userMapper.fromUserRequest(createOrUpdateUserDTO, user);

        userRepository.save(user);
    }

    public void updateUser(long userId, CreateOrUpdateUserAdminDTO createOrUpdateUserAdminDTO) throws Exception {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not with id=" + userId + " not found"));

        var unprocessableReasons = getUnprocessableReasonsExcludingExistingUser(createOrUpdateUserAdminDTO.getUsername(), createOrUpdateUserAdminDTO.getEmail(), userId);
        if (!unprocessableReasons.isEmpty())
            throw new UnprocessableEntityException(unprocessableReasons);

        userMapper.fromUserAdmRequest(createOrUpdateUserAdminDTO, user);

        userRepository.save(user);
    }

    private List<String> getUnprocessableReasonsExcludingExistingUser(String username, String email, Long existingUserId) {
        var unprocessableReasons = new ArrayList<String>();

        if (isUsernameAlreadyRegisteredExcludingExistingUser(username, existingUserId)) {
            String msg = String.format("Username [%s] is already registered", username);
            unprocessableReasons.add(msg);
        }

        if (isEmailAlreadyRegisteredExcludingExistingUser(email, existingUserId)) {
            String msg = String.format("Email [%s] is already registered", email);
            unprocessableReasons.add(msg);
        }

        return unprocessableReasons;
    }

    private boolean isUsernameAlreadyRegisteredExcludingExistingUser(String username, Long existingUserId) {
        return userRepository.existsByUsernameAndIdNot(username, existingUserId);
    }

    private boolean isEmailAlreadyRegisteredExcludingExistingUser(String email, Long existingUserId) {
        return userRepository.existsByEmailAndIdNot(email, existingUserId);
    }

    public void deleteUser(long userId) throws Exception {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not with id=" + userId + " not found"));

        userRepository.delete(user);
    }

    public boolean isUsernameAlreadyRegistered(String username) {
        return userRepository.existsByUsername(username);

    }

    public boolean isEmailAlreadyRegistered(String email) {
        return userRepository.existsByEmail(email);

    }

}
