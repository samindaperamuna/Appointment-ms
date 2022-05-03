package ms.asp.appointment.mappers;

import ms.asp.appointment.domain.Participant;
import ms.asp.appointment.models.ParticipantModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ParticipantMapper {
  @Mapping(source="participantInfo.name", target = "name")
  ParticipantModel map(Participant participant);

  @Mapping(source="name", target = "participantInfo.name")
  Participant map(ParticipantModel participant);
}
