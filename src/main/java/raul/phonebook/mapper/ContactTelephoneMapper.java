package raul.phonebook.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import raul.phonebook.dto.contact.telephone.ContactTelephoneDTO;
import raul.phonebook.dto.contact.telephone.CreateOrUpdateContactTelephoneDTO;
import raul.phonebook.exception.NotFoundException;
import raul.phonebook.model.contact.ContactTelephone;
import raul.phonebook.model.types.TelephoneType;
import raul.phonebook.repository.TelephoneTypeRepository;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ContactTelephoneMapper {

    @Autowired
    protected TelephoneTypeRepository telephoneTypeRepository;

    @Mapping(source = "telephoneNumber", target = "ddd", qualifiedByName = "getDddFromTelephone")
    @Mapping(source = "telephoneNumber", target = "telephoneOnlyNumbers", qualifiedByName = "getOnlyNumbersFromTelephone")
    @Mapping(source = "telephoneNumber", target = "telephoneFormatted")
    @Mapping(source = "type", target = "type", qualifiedByName = "getTelephoneTypeFromDb")
    public abstract ContactTelephone toTelephone(CreateOrUpdateContactTelephoneDTO telReq) throws Exception;

    @Mapping(source = "telephoneFormatted", target = "telephoneNumber")
    @Mapping(source = "type.id", target = "type")
    public abstract ContactTelephoneDTO toContactTelephoneResp(ContactTelephone tel);

    public abstract Set<ContactTelephone> telephoneReqsToTelephones(List<CreateOrUpdateContactTelephoneDTO> tels);

    public abstract List<ContactTelephoneDTO> telephoneToTelephonesResp(List<ContactTelephone> tels);

    public Set<ContactTelephone> fromTelReqs(List<CreateOrUpdateContactTelephoneDTO> telReqs, @MappingTarget Set<ContactTelephone> tels) throws Exception {
        var newTels = new HashMap<String, ContactTelephone>();
        var telReqsNumbers = telReqs.stream()
                .map(CreateOrUpdateContactTelephoneDTO::getTelephoneNumber)
                .collect(Collectors.toList());

        for (ContactTelephone currentTel : tels) {
            var currTelFormatted = currentTel.getTelephoneFormatted();
            if (telReqsNumbers.contains(currTelFormatted))
                newTels.put(currTelFormatted, currentTel);
        }

        for (CreateOrUpdateContactTelephoneDTO currentTelReq : telReqs) {
            var currTelReqNumbr = currentTelReq.getTelephoneNumber();

            if (!newTels.containsKey(currTelReqNumbr)) {
                newTels.put(currTelReqNumbr, this.toTelephone(currentTelReq));
                continue;
            }

            var currentTelModel = newTels.get(currTelReqNumbr);
            if (!currentTelModel.getType().getId().equals(currentTelReq.getType()))
                currentTelModel.setType(getTelephoneTypeFromDb(currentTelReq.getType()));

            if (!currentTelModel.getDescription().equals(currentTelReq.getDescription()))
                currentTelModel.setDescription(currentTelReq.getDescription());
        }

        return new HashSet<>(newTels.values());
    }

    //region NAMED METHODS
    @Named("getDddFromTelephone")
    public String getDddFromTelephone(String telephoneNumber) {
        return telephoneNumber.substring(1, 3);
    }

    @Named("getOnlyNumbersFromTelephone")
    public String getOnlyNumbersFromTelephone(String telephoneNumber) {
        return telephoneNumber.split(" ")[1].replace("-", "");
    }

    @Named("getTelephoneTypeFromDb")
    public TelephoneType getTelephoneTypeFromDb(Long typeId) {
        return telephoneTypeRepository.findById(typeId)
                .orElseThrow(() -> new NotFoundException("Telephone type with id=" + typeId + " does not exist"));
    }
    //endregion

}
