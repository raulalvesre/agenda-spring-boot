package raul.phonebook.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import raul.phonebook.validation.Email;
import raul.phonebook.validation.Password;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class CreateOrUpdateUserAdminDTO {

    @Size(min = 3, max = 200, message = "Name size must be between 3 and 200")
    @NotBlank(message = "Name must not be blank")
    @JsonProperty
    private String name;

    @Size(min = 3, max = 16, message = "Username size must be between 3 and 16")
    @NotBlank(message = "Username must not be blank")
    @JsonProperty
    private String username;

    @Email
    @JsonProperty
    private String email;

    @Password
    @JsonProperty
    private String password;

    @NotNull(message = "Role must not be null")
    @JsonProperty
    private Long role;

}
