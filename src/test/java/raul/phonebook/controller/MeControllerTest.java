package raul.phonebook.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
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
@WithUserDetails(value = "rar", userDetailsServiceBeanName = "myUserDetailsService")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MeControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final String controllerPath = "/me";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_retrieve_current_user() throws Exception {
        mockMvc.perform(get(controllerPath)
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
    void should_update_current_user() throws Exception {
        var newMe = UserDTOFactory.buildValidCreateOrUpdateUserDTO();

        mockMvc.perform(put(controllerPath)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newMe)))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_delete_current_user() throws Exception {
        mockMvc.perform(delete(controllerPath))
                .andExpect(status().isNoContent());
    }

}
