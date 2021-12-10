package raul.phonebook.model.base;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class Token<T, U> {

    @Id
    protected T token;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", nullable = false)
    protected U owner;

    @NotNull
    @CreatedDate
    @Column
    protected LocalDateTime createdDate;

    public Token(T token, U owner) {
        this.token = token;
        this.owner = owner;

    }

    @PrePersist
    private void prePersist() {
        createdDate = LocalDateTime.now();
    }

}
