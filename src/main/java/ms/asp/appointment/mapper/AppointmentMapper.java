package ms.asp.appointment.mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ms.asp.appointment.domain.Appointment;
import ms.asp.appointment.domain.AppointmentHistory;
import ms.asp.appointment.domain.Participant;
import ms.asp.appointment.model.AppointmentModel;

@Mapper(componentModel = "spring", uses = { CodeableMapper.class })
public interface AppointmentMapper extends BaseMapper<Appointment, AppointmentModel> {

    @Mapping(source = "appointment.id", target = "appointmentId")
    @Mapping(target = "id", ignore = true)
    AppointmentHistory toHistory(Appointment appointment);

    default Instant map(LocalDateTime dateTime) {
	return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }

    default String map(Participant participant) {
	return Objects.toString(participant);
    }
}
