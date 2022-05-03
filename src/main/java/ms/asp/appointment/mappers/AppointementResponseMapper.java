package ms.asp.appointment.mappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import ms.asp.appointment.domain.Appointement;
import ms.asp.appointment.domain.CodeableConcept;
import ms.asp.appointment.domain.Participant;
import ms.asp.appointment.models.AppointmentsHistoryResponse;
import ms.asp.appointment.models.AppointementResponse;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED, uses = {CodeableMapper.class})
public interface AppointementResponseMapper extends BaseMapper<Appointement, AppointementResponse>{



  AppointmentsHistoryResponse toHistory(Appointement appointement);

  default Instant map(LocalDateTime dateTime){
    return dateTime.atZone(ZoneId.systemDefault()).toInstant();
  }

  default String map(Participant participant){
    return Objects.toString(participant);
  }


}
