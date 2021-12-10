package raul.phonebook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import raul.phonebook.dto.auth.ChangePasswordRequest;
import raul.phonebook.dto.auth.ConfirmEmailRequest;
import raul.phonebook.dto.auth.LoginRequest;
import raul.phonebook.dto.auth.SendRecoveryPasswordTokenOnEmailRequest;
import raul.phonebook.factory.UserDTOFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static raul.phonebook.factory.CustomMatchersFactory.localDateTimeAsStringIsWithin;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final String controllerPath = "/auth";
    private final String bigString = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(scripts = "classpath:sql/cleanUsers.sql")
    void should_register_user() throws Exception {
        var newUser = UserDTOFactory.buildValidCreateOrUpdateUserDTO();

        mockMvc.perform(post(controllerPath + "/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(newUser.getName())))
                .andExpect(jsonPath("$.username", is(newUser.getUsername())))
                .andExpect(jsonPath("$.email", is(newUser.getEmail())))
                .andExpect(jsonPath("$.createdDate", localDateTimeAsStringIsWithin(10, ChronoUnit.SECONDS, LocalDateTime.now())))
                .andExpect(jsonPath("$.lastModifiedDate", nullValue()));
    }

    @Test
    void should_login() throws Exception {
        var loginReq = new LoginRequest("rar", "rar432");

        mockMvc.perform(post(controllerPath + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", Matchers.notNullValue()));
    }

    @Test
    @Sql(scripts = { "classpath:sql/cleanUsers.sql", "classpath:sql/createDisabledUserAndItsEmailConfirmationToken.sql" })
    void should_confirm_email() throws Exception {
        var confirmEmailReq = new ConfirmEmailRequest("confirmemailtesttoken");

        mockMvc.perform(post(controllerPath + "/confirm-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(confirmEmailReq)))
                .andExpect(status().isOk());
    }

    @Test
    void should_send_recovery_token_on_email() throws Exception {
        var sendRecoveryPasswordReq = new SendRecoveryPasswordTokenOnEmailRequest("raul.alves.re@gmail.com");

        mockMvc.perform(post(controllerPath + "/receive-password-recovery-token-on-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(sendRecoveryPasswordReq)))
                .andExpect(status().isAccepted());
    }

    @Test
    @Sql(scripts = { "classpath:sql/createPasswordRecoveryTokenToAdm.sql" })
    void should_return_no_content_when_changing_password() throws Exception {
        var passwordRecoveryToken = new ChangePasswordRequest("changepasstoken", "rar123");

        mockMvc.perform(put(controllerPath + "/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(passwordRecoveryToken)))
                .andExpect(status().isNoContent());
    }

}
