package ms.asp.appointment.mapper;

import static ms.asp.appointment.util.CommonUtils.generatePublicId;

import java.util.List;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ms.asp.appointment.domain.Appointment;
import ms.asp.appointment.domain.AppointmentHistory;
import ms.asp.appointment.domain.Note;
import ms.asp.appointment.domain.Participant;
import ms.asp.appointment.domain.ServiceProvider;
import ms.asp.appointment.model.AppointmentHistoryModel;
import ms.asp.appointment.util.JSONUtils;

@Mapper(config = BaseMapper.class)
public interface AppointmentHistoryMapper extends BaseMapper<AppointmentHistory, AppointmentHistoryModel> {

    @InheritConfiguration
    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "mapPublicIdAppointmentHistoty")
    AppointmentHistory toEntity(AppointmentHistoryModel model);

    @Named("mapPublicIdAppointmentHistoty")
    default String mapPublicId(String publicId) {
	if (publicId == null || publicId.isBlank()) {
	    return generatePublicId();
	}

	return publicId;
    }

    @Mapping(source = "appointment.id", target = "appointmentId")
    @Mapping(source = "period.start", target = "start")
    @Mapping(source = "period.end", target = "end")
    @Mapping(source = "serviceProvider", target = "serviceProviderJSON")
    @Mapping(source = "participants", target = "participantsJSON")
    @Mapping(source = "notes", target = "notesJSON")
    AppointmentHistory toHistory(Appointment appointment);

    default String toServiceProviderJSON(ServiceProvider serviceProvider) {
	return JSONUtils.objectToJSON(serviceProvider);
    }

    default String toParticipantJSON(List<Participant> participants) {
	return JSONUtils.objectToJSON(participants);
    }

    default String toNotesJSON(List<Note> notes) {
	return JSONUtils.objectToJSON(notes);
    }
}
