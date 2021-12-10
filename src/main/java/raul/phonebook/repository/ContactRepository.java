package raul.phonebook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import raul.phonebook.model.contact.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long>, JpaSpecificationExecutor<Contact> {

    boolean existsByOwnerIdAndTelephonesTelephoneFormatted(long ownerId, String telephoneNumber);
    boolean existsByOwnerUsernameAndTelephonesTelephoneFormatted(String ownerUsername, String telephoneNumber);
    boolean existsByIdNotAndTelephonesTelephoneFormatted(long existingContactId, String telephoneNumber);

    default Page<Contact> findAllByOwnerUsername(String ownerUsername, Specification<Contact> spec, Pageable pageable) {
        return findAll(Specification.where(ownerUsernameEquals(ownerUsername)).and(spec), pageable);
    }

    private Specification<Contact> ownerUsernameEquals(String ownerUsername) {
        return (root, query, criteriaBuilder)
            -> criteriaBuilder.equal(root.get("owner").get("username"), ownerUsername);
    }

}
