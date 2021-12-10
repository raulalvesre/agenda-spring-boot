package raul.phonebook.dto.contact.telephone;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import raul.phonebook.validation.Telephone;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@Telephone
public class CreateOrUpdateContactTelephoneDTO {

    @NotNull(message = "Telephone type must not be null")
    @JsonProperty
    private Long type;

    @Size(max = 50, message = "Telephone description size must less than 50")
    @NotBlank(message = "Telephone description must not be blank")
    @JsonProperty
    private String description;

    @NotNull(message = "Telephone number must no be null")
    @JsonProperty
    private String telephoneNumber;

}
