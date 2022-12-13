package ms.asp.appointment.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ms.asp.appointment.domain.Appointment;
import ms.asp.appointment.model.AppointmentModel;

@Mapper(config = BaseMapper.class,
	uses = { AppointmentHistoryMapper.class,
		ContactMapper.class,
		ParticipantMapper.class,
		NoteMapper.class,
		PeriodMapper.class })
public interface AppointmentMapper extends BaseMapper<Appointment, AppointmentModel> {

    @InheritConfiguration
    @Mapping(source = "requestedPeriod", target = "period")
    Appointment toEntity(AppointmentModel model);

    @Mapping(source = "period", target = "requestedPeriod")
    AppointmentModel toModel(Appointment appointment);
}
