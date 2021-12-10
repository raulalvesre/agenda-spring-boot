package raul.phonebook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import raul.phonebook.model.ConfirmEmailToken;
import raul.phonebook.model.PasswordRecoveryToken;

@Repository
public interface ConfirmEmailTokenRepository extends CrudRepository<ConfirmEmailToken, String> {
}
