package raul.phonebook.model.contact;

import lombok.*;
import raul.phonebook.model.base.Record;
import raul.phonebook.model.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "contacts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact extends Record {

    @Size(max = 200)
    @NotBlank
    @Column(nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "contact_id", nullable = false)
    private Set<ContactTelephone> telephones;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private User owner;

}
