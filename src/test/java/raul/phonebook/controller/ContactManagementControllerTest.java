package raul.phonebook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
import raul.phonebook.factory.ContactDTOFactory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static raul.phonebook.factory.CustomMatchersFactory.isAJSONArrayOfNulls;
import static raul.phonebook.factory.CustomMatchersFactory.localDateTimesAsJSONArrayAreWithin;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"classpath:sql/cleanContacts.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ContactManagementControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final String controllerPath = "/contact-management";
    private final String bigString = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(scripts = "classpath:sql/addContactsToAdm.sql")
    void should_retrieve_contact_by_id() throws Exception {
        mockMvc.perform(get(controllerPath + "/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("contact1")))
                .andExpect(jsonPath("$.telephones[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$.telephones[*].type", containsInAnyOrder(1, 1)))
                .andExpect(jsonPath("$.telephones[*].description", containsInAnyOrder("tel1", "tel2")))
                .andExpect(jsonPath("$.telephones[*].telephoneNumber", containsInAnyOrder("(11) 97645-4631", "(11) 97645-4632")))
                .andExpect(jsonPath("$.telephones[*].createdDate", localDateTimesAsJSONArrayAreWithin(2, ChronoUnit.SECONDS, LocalDateTime.now())))
                .andExpect(jsonPath("$.telephones[*].lastModifiedDate", isAJSONArrayOfNulls()))
                .andExpect(jsonPath("$.ownerId", is(1)));
    }

    @Test
    void not_found_when_user_does_not_exist_on_get() throws Exception {
        mockMvc.perform(get(controllerPath + "/7")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "classpath:sql/addContactsToAdm.sql")
    void should_return_page() throws Exception {
        mockMvc.perform(get(controllerPath)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.totalElements", is(2)))
                .andExpect(jsonPath("$.size", is(500)))
                .andExpect(jsonPath("$.numberOfElements", is(2)))
                .andExpect(jsonPath("$.content[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$.content[*].name", containsInAnyOrder("contact1", "contact2")))
                .andExpect(jsonPath("$.content[*].telephones[*].id", containsInAnyOrder(1, 2, 3, 4)))
                .andExpect(jsonPath("$.content[*].telephones[*].type", containsInAnyOrder(1, 1, 1, 1)))
                .andExpect(jsonPath("$.content[*].telephones[*].description", containsInAnyOrder("tel1", "tel2", "tel3", "tel4")))
                .andExpect(jsonPath("$.content[*].telephones[*].telephoneNumber", containsInAnyOrder("(11) 97645-4631", "(11) 97645-4632", "(11) 97645-4633", "(11) 97645-4634")))
                .andExpect(jsonPath("$.content[*].telephones[*].createdDate", localDateTimesAsJSONArrayAreWithin(2, ChronoUnit.SECONDS, LocalDateTime.now())))
                .andExpect(jsonPath("$.content[0].telephones[*].lastModifiedDate", isAJSONArrayOfNulls()));
    }

    @Test
    void should_add_new_contact() throws Exception {
        var newContact = ContactDTOFactory.buildValidCreateOrUpdateContactAdminDTO();

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newContact)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("contact")))
                .andExpect(jsonPath("$.telephones[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$.telephones[*].type", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$.telephones[*].description", containsInAnyOrder("tel1", "tel2")))
                .andExpect(jsonPath("$.telephones[*].telephoneNumber", containsInAnyOrder("(11) 97645-4630", "(11) 4002-8922")))
                .andExpect(jsonPath("$.telephones[*].createdDate", localDateTimesAsJSONArrayAreWithin(2, ChronoUnit.SECONDS, LocalDateTime.now())))
                .andExpect(jsonPath("$.telephones[*].lastModifiedDate", isAJSONArrayOfNulls()))
                .andExpect(jsonPath("$.ownerId", is(1)));
    }

    @Test
    @Sql(scripts = "classpath:sql/addContactsToAdm.sql")
    void should_update_contact() throws Exception {
        var contact = ContactDTOFactory.buildValidCreateOrUpdateContactAdminDTO();

        mockMvc.perform(put(controllerPath + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(contact)))
                .andExpect(status().isNoContent());
    }

    @Test
    void not_found_when_contact_does_not_exist_on_put() throws Exception {
        var contact = ContactDTOFactory.buildValidCreateOrUpdateContactAdminDTO();

        mockMvc.perform(put(controllerPath + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(contact)))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "classpath:sql/addContactsToAdm.sql")
    void should_delete_contact() throws Exception {
        mockMvc.perform(delete(controllerPath + "/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void not_found_when_contact_does_not_exist_on_delete() throws Exception {
        mockMvc.perform(delete(controllerPath + "/7"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(scripts = "classpath:sql/addContactsToAdm.sql")
    void should_return_true_when_telephone_already_registered() throws Exception {
        mockMvc.perform(get(controllerPath + "/is-telephone-registered")
                        .param("contactOwnerId", "1")
                        .param("telephone", "(11) 97645-4631")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void should_return_false_when_telephone_is_not_registered() throws Exception {
        mockMvc.perform(get(controllerPath + "/is-telephone-registered")
                        .param("contactOwnerId", "1")
                        .param("telephone", "(11) 97645-4630")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"\t", "\n", bigString})
    void should_validate_bad_contact_requests_with_invalid_name_field(String name) throws Exception {
        var newContact = ContactDTOFactory.buildValidCreateOrUpdateContactAdminDTO();
        newContact.setName(name);

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newContact)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = "classpath:sql/addContactsToAdm.sql")
    void unprocessable_with_contact_telephone_already_registered_on_post() throws Exception {
        var newContact = ContactDTOFactory.buildValidCreateOrUpdateContactAdminDTO();
        newContact.getTelephones().get(0).setTelephoneNumber("(11) 97645-4631");

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newContact)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @Sql(scripts = "classpath:sql/addContactsToAdm.sql")
    void unprocessable_with_contact_with_telephone_already_registered_on_put() throws Exception {
        var contact = ContactDTOFactory.buildValidCreateOrUpdateContactAdminDTO();
        contact.getTelephones().get(0).setTelephoneNumber("(11) 97645-4633");

        mockMvc.perform(put(controllerPath + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(contact)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void should_validate_bad_contact_request_with_duplicate_telephone_numbers() throws Exception {
        var newContact = ContactDTOFactory.buildValidCreateOrUpdateContactAdminDTO();
        newContact.getTelephones().get(1).setTelephoneNumber(newContact.getTelephones().get(0).getTelephoneNumber());

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newContact)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_validate_bad_contact_request_with_null_telephone_type() throws Exception {
        var newContact = ContactDTOFactory.buildValidCreateOrUpdateContactAdminDTO();
        newContact.getTelephones().get(1).setType(null);

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newContact)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void not_found_with_contact_with_non_existent_telephone_type() throws Exception {
        var newContact = ContactDTOFactory.buildValidCreateOrUpdateContactAdminDTO();
        newContact.getTelephones().get(1).setType(7L);

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newContact)))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"\t", "\n", bigString})
    void should_validate_bad_contact_request_with_invalid_telephone_description(String telDesc) throws Exception {
        var newContact = ContactDTOFactory.buildValidCreateOrUpdateContactAdminDTO();
        newContact.getTelephones().get(1).setDescription(telDesc);

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newContact)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void should_validate_bad_contact_request_with_null_telephone_number(String telNmbr) throws Exception {
        var newContact = ContactDTOFactory.buildValidCreateOrUpdateContactAdminDTO();
        newContact.getTelephones().get(1).setTelephoneNumber(telNmbr);

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newContact)))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @CsvSource({
            "\t,1", "\t,2", "\t,3",
            "\n,1", "\n,2","\n,3",
            "notatel,1",
            "(11) 4002-8922,1",
            "notatel,2",
            "(11) 97645-4630,1",
            "notatel,3"
    })
    void should_validate_bad_contact_request_with_invalid_telephone_number(String telNmbr, String telType) throws Exception {
        var newContact = ContactDTOFactory.buildValidCreateOrUpdateContactAdminDTO();
        newContact.getTelephones().get(1).setType(Long.parseLong(telType));
        newContact.getTelephones().get(1).setTelephoneNumber(telNmbr);

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newContact)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_validate_bad_contact_request_with_null_owner() throws Exception {
        var newContact = ContactDTOFactory.buildValidCreateOrUpdateContactAdminDTO();
        newContact.setOwner(null);

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newContact)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void not_found_with_contact_with_non_existent_owner() throws Exception {
        var newContact = ContactDTOFactory.buildValidCreateOrUpdateContactAdminDTO();
        newContact.setOwner(7L);

        mockMvc.perform(post(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newContact)))
                .andExpect(status().isNotFound());
    }


}
