package raul.phonebook.repository;

import org.springframework.data.repository.CrudRepository;
import raul.phonebook.model.types.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role getById(Long roleId);
}
