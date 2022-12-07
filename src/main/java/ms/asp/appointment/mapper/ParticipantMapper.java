package ms.asp.appointment.mapper;

import static ms.asp.appointment.util.CommonUtils.generatePublicId;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ms.asp.appointment.domain.Participant;
import ms.asp.appointment.model.ParticipantModel;

@Mapper(uses = { ContactMapper.class })
public interface ParticipantMapper {

    @Mapping(source = "participantInfo.name", target = "name")
    @Mapping(source = "participantInfo.contact", target = "contact")
    @Mapping(source = "type", target = "type")
    ParticipantModel map(Participant participant);

    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "participantPublicId")
    @Mapping(source = "name", target = "participantInfo.name")
    @Mapping(source = "contact", target = "participantInfo.contact")
    @Mapping(source = "type", target = "type")
    Participant map(ParticipantModel participant);

    Collection<ParticipantModel> toModel(Collection<Participant> participant);

    @Named("participantPublicId")
    default String publicId(String publicId) {
	if (publicId == null || publicId.isBlank()) {
	    return generatePublicId();
	}

	return publicId;
    }
}
