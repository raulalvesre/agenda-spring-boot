package raul.phonebook.dto.contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import raul.phonebook.dto.contact.telephone.CreateOrUpdateContactTelephoneDTO;
import raul.phonebook.validation.UniqueTelephones;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CreateOrUpdateContactDTO {

    @Size(min = 3, max = 200, message = "Name size must be between 3 and 200")
    @NotBlank(message = "Name must not be blank")
    @JsonProperty
    private String name;

    @Valid
    @UniqueTelephones
    @JsonProperty
    private List<CreateOrUpdateContactTelephoneDTO> telephones;

}
