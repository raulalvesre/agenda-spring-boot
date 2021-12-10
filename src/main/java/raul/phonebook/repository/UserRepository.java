package raul.phonebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import raul.phonebook.model.user.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsernameAndIdNot(String username, long existingUserId);

    boolean existsByEmailAndIdNot(String email, long existingUserId);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    User getByUsername(String username);
    User getByEmail(String email);
}
