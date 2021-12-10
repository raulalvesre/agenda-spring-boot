package raul.phonebook.model.types;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raul.phonebook.model.base.Type;
import raul.phonebook.model.user.User;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role extends Type {

    @OneToMany(mappedBy = "role")
    private Set<User> users;

}
