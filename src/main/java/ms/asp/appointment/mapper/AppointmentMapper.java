package ms.asp.appointment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ms.asp.appointment.domain.Appointment;
import ms.asp.appointment.domain.AppointmentHistory;
import ms.asp.appointment.domain.Participant;
import ms.asp.appointment.domain.Slot;
import ms.asp.appointment.model.AppointmentModel;
import ms.asp.appointment.model.ParticipantModel;
import ms.asp.appointment.model.SlotModel;

@Mapper(componentModel = "spring", uses = { CodeableMapper.class })
public interface AppointmentMapper extends BaseMapper<Appointment, AppointmentModel> {

    @Mapping(source = "appointment.id", target = "appointmentId")
    @Mapping(target = "id", ignore = true)
    AppointmentHistory toHistory(Appointment appointment);

    @Mapping(source = "participantInfo.name", target = "name")
    @Mapping(source = "participantInfo.contact", target = "contact")
    @Mapping(source = "type", target = "type")
    ParticipantModel map(Participant participant);

    @Mapping(source = "name", target = "participantInfo.name")
    @Mapping(source = "contact", target = "participantInfo.contact")
    @Mapping(source = "type", target = "type")
    @Mapping(target = "publicId", ignore = true)
    Participant map(ParticipantModel participant);

    @Mapping(target = "publicId", ignore = true)
    Slot slotModelToSlot(SlotModel slotModel);
}
