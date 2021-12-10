package raul.phonebook.model.contact;

import lombok.*;
import raul.phonebook.model.base.Record;
import raul.phonebook.model.types.TelephoneType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "contacts_telephones")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactTelephone extends Record {

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private TelephoneType type;

    @Size(max = 200)
    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotNull
    @Column(nullable = false)
    private Integer ddd;

    @Size(max = 9)
    @NotBlank
    @Column(nullable = false)
    private String telephoneOnlyNumbers;

    @Size(max = 15)
    @NotBlank
    @Column(nullable = false)
    private String telephoneFormatted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof  ContactTelephone)) return false;
        return telephoneFormatted != null && telephoneFormatted.equals(((ContactTelephone) o).getTelephoneFormatted());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
