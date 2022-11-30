package ms.asp.appointment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ms.asp.appointment.domain.Participant;
import ms.asp.appointment.model.ParticipantModel;

@Mapper
public interface ParticipantMapper {

    @Mapping(source = "participantInfo.name", target = "name")
    ParticipantModel map(Participant participant);

    @Mapping(source = "name", target = "participantInfo.name")
    Participant map(ParticipantModel participant);
}
