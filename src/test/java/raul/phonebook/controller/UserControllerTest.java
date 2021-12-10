package raul.phonebook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import raul.phonebook.factory.UserDTOFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static raul.phonebook.factory.CustomMatchersFactory.localDateTimeAsStringIsWithin;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final String controllerPath = "/users";
    private final String bigString = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_retrieve_user_by_id() throws Exception {
        mockMvc.perform(get(controllerPath + "/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("rar")))
                .andExpect(jsonPath("$.username", is("rar")))
                .andExpect(jsonPath("$.email", is("raul.alves.re@gmail.com")))
                .andExpect(jsonPath("$.role", is(1)))
                .andExpect(jsonPath("$.createdDate", localDateTimeAsStringIsWithin(2, ChronoUnit.SECONDS, LocalDateTime.now())))
                .andExpect(jsonPath("$.lastModifiedDate", nullValue()));
    }

    @Test
    void not_found_when_user_does_not_exist_on_get() throws Exception {
        mockMvc.perform(get(controllerPath + "/7")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "classpath:sql/cleanUsers.sql")
    void should_add_new_user() throws Exception {
        var newUser = UserDTOFactory.buildValidCreateOrUpdateUserAdminDTO();

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newUser)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("name")))
                .andExpect(jsonPath("$.username", is("username")))
                .andExpect(jsonPath("$.email", is("email@gmail.com")))
                .andExpect(jsonPath("$.role", is(1)))
                .andExpect(jsonPath("$.createdDate", localDateTimeAsStringIsWithin(2, ChronoUnit.SECONDS, LocalDateTime.now())))
                .andExpect(jsonPath("$.lastModifiedDate", nullValue()));
    }

    @Test
    void should_update_user() throws Exception {
        var user = UserDTOFactory.buildValidCreateOrUpdateUserAdminDTO();

        mockMvc.perform(put(controllerPath + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(user)))
                .andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = "classpath:sql/cleanUsers.sql")
    void not_found_when_user_does_not_exist_on_put() throws Exception {
        var user = UserDTOFactory.buildValidCreateOrUpdateUserAdminDTO();

        mockMvc.perform(put(controllerPath + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_delete_user() throws Exception {
        mockMvc.perform(delete(controllerPath + "/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void not_found_when_user_does_not_exist_on_delete() throws Exception {
        mockMvc.perform(delete(controllerPath + "/7"))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_return_true_when_username_already_registered() throws Exception {
        mockMvc.perform(get(controllerPath + "/is-username-registered")
                        .param("username", "rar")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @Sql(scripts = "classpath:sql/cleanUsers.sql")
    void should_return_false_when_username_is_not_registered() throws Exception {
        mockMvc.perform(get(controllerPath + "/is-username-registered")
                        .param("username", "rar")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void should_return_true_when_email_already_registered() throws Exception {
        mockMvc.perform(get(controllerPath + "/is-email-registered")
                        .param("email", "raul.alves.re@gmail.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @Sql(scripts = "classpath:sql/cleanUsers.sql")
    void should_return_false_when_email_is_not_registered() throws Exception {
        mockMvc.perform(get(controllerPath + "/is-email-registered")
                        .param("email", "raul.alves.re@gmail.com")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void unprocessable_with_user_with_username_already_registered_on_post() throws Exception {
        var user = UserDTOFactory.buildValidCreateOrUpdateUserAdminDTO();
        user.setUsername("rar");

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(user)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void unprocessable_with_user_with_username_already_registered_on_put() throws Exception {
        var user = UserDTOFactory.buildValidCreateOrUpdateUserAdminDTO();
        user.setUsername("rar");

        mockMvc.perform(put(controllerPath + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(user)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void unprocessable_with_user_with_email_already_registered_on_post() throws Exception {
        var user = UserDTOFactory.buildValidCreateOrUpdateUserAdminDTO();
        user.setEmail("raul.alves.re@gmail.com");

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(user)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void unprocessable_with_user_with_email_already_registered_on_put() throws Exception {
        var user = UserDTOFactory.buildValidCreateOrUpdateUserAdminDTO();
        user.setEmail("raul.alves.re@gmail.com");

        mockMvc.perform(put(controllerPath + "/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(user)))
                .andExpect(status().isUnprocessableEntity());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"\t", "\n", bigString})
    void should_validate_bad_user_requests_with_invalid_name_field(String name) throws Exception {
        var newUser = UserDTOFactory.buildValidCreateOrUpdateUserAdminDTO();
        newUser.setName(name);

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"\t", "\n", bigString})
    void should_validate_bad_user_requests_with_invalid_username_field(String username) throws Exception {
        var newUser = UserDTOFactory.buildValidCreateOrUpdateUserAdminDTO();
        newUser.setUsername(username);

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"\t", "\n", "not-an-email", "thejpaemailannotationsucks@email"})
    void should_validate_bad_user_requests_with_invalid_email_field(String email) throws Exception {
        var newUser = UserDTOFactory.buildValidCreateOrUpdateUserAdminDTO();
        newUser.setEmail(email);

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"\t", "\n", "i-have-no-digits", "aa1", bigString})
    void should_validate_bad_user_requests_with_invalid_password_field(String password) throws Exception {
        var newUser = UserDTOFactory.buildValidCreateOrUpdateUserAdminDTO();
        newUser.setPassword(password);

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_validate_bad_user_request_with_null_role() throws Exception {
        var newUser = UserDTOFactory.buildValidCreateOrUpdateUserAdminDTO();
        newUser.setRole(null);

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void not_found_with_user_request_with_non_existent_role() throws Exception {
        var newUser = UserDTOFactory.buildValidCreateOrUpdateUserAdminDTO();
        newUser.setRole(1000L);

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newUser)))
                .andExpect(status().isNotFound());
    }

}
