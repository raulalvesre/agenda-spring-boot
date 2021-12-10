package raul.phonebook.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raul.phonebook.validation.Email;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendRecoveryPasswordTokenOnEmailRequest {

    @Email
    private String email;

}
