package ms.asp.appointment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ms.asp.appointment.domain.Participant;
import ms.asp.appointment.model.ParticipantModel;

@Mapper
public interface ParticipantMapper {

    @Mapping(source = "participantInfo.name", target = "name")
    @Mapping(source = "participantInfo.contact", target = "contact")
    @Mapping(source = "type", target = "type")
    ParticipantModel map(Participant participant);

    @Mapping(source = "name", target = "participantInfo.name")
    @Mapping(source = "contact", target = "participantInfo.contact")
    @Mapping(source = "type", target = "type")
    @Mapping(target = "publicId", ignore = true)
    Participant map(ParticipantModel participant);
}
