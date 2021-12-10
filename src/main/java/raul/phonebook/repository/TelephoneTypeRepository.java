package raul.phonebook.repository;

import org.springframework.data.repository.CrudRepository;
import raul.phonebook.model.types.TelephoneType;

public interface TelephoneTypeRepository extends CrudRepository<TelephoneType, Long> {
}
