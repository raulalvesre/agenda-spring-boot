package raul.phonebook.model.types;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raul.phonebook.model.base.Type;
import raul.phonebook.model.contact.ContactTelephone;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "telephone_types")
@Getter
@Setter
@NoArgsConstructor
public class TelephoneType extends Type {

    @OneToMany(mappedBy = "type")
    private Set<ContactTelephone> contactTelephones;

}
