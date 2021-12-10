package raul.phonebook.dto.contact;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raul.phonebook.dto.contact.telephone.ContactTelephoneDTO;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonPropertyOrder({ "id", "ownerId", "name", "telephones", "createdDate", "lastModifiedDate" })
public class ContactAdminDTO {

    private Long id;
    private String name;
    private List<ContactTelephoneDTO> telephones;
    private Long ownerId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastModifiedDate;

}
