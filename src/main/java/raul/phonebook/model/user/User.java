package raul.phonebook.model.user;

import lombok.*;
import raul.phonebook.model.base.Record;
import raul.phonebook.model.types.Role;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends Record {

    @Size(max = 200)
    @NotBlank
    @Column(nullable = false)
    private String name;

    @Size(max = 200)
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @Size(max = 16)
    @NotBlank
    @Column(unique = true, nullable = false)
    private String username;

    @Size(max = 500)
    @NotBlank
    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", nullable = false)
    private Role role;

    @NotNull
    @Column
    private boolean enabled;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id != null && id.equals(((User) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}

