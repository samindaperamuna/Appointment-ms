package ms.asp.appointment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ms.asp.appointment.domain.Appointment;
import ms.asp.appointment.domain.AppointmentHistory;
import ms.asp.appointment.model.AppointmentModel;

@Mapper(uses = { ParticipantMapper.class, AppointmentSlotMapper.class })
public interface AppointmentMapper extends BaseMapper<Appointment, AppointmentModel> {

    @Mapping(source = "appointment.id", target = "appointmentId")
    @Mapping(target = "id", ignore = true)
    AppointmentHistory toHistory(Appointment appointment);
}
