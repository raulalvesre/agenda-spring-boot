package raul.phonebook.model;

import lombok.NoArgsConstructor;
import raul.phonebook.model.base.Token;
import raul.phonebook.model.user.User;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class PasswordRecoveryToken extends Token<String, User> {

    public PasswordRecoveryToken(String token, User user) {
        super(token, user);
    }

}
