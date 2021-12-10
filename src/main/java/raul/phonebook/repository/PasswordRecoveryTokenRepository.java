package raul.phonebook.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import raul.phonebook.model.PasswordRecoveryToken;

@Repository
public interface PasswordRecoveryTokenRepository extends CrudRepository<PasswordRecoveryToken, String> {
}
