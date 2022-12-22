package ms.asp.appointment.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ms.asp.appointment.domain.Appointment;
import ms.asp.appointment.domain.Participant;
import ms.asp.appointment.domain.ParticipantInfo;
import ms.asp.appointment.domain.ServiceProvider;
import ms.asp.appointment.model.appointment.AppointmentModel;
import ms.asp.appointment.model.appointment.ParticipantInfoModel;
import ms.asp.appointment.model.appointment.ParticipantModel;
import ms.asp.appointment.model.appointment.ServiceProviderModel;

@Mapper(
	config = BaseMapper.class,
	uses = { AppointmentHistoryMapper.class,
		ContactMapper.class,
		NoteMapper.class,
		PeriodMapper.class })
public interface AppointmentMapper extends BaseMapper<Appointment, AppointmentModel> {

    @InheritConfiguration
    @Mapping(source = "requestedPeriod", target = "period")
    Appointment toEntity(AppointmentModel model);

    @InheritConfiguration
    @Mapping(source = "period", target = "requestedPeriod")
    AppointmentModel toModel(Appointment appointment);

    @InheritConfiguration
    ServiceProviderModel toModel(ServiceProvider serviceProvider);

    @InheritConfiguration
    @Mapping(target = "publicId", qualifiedByName = "mapPublicId")
    ServiceProvider toEntity(ServiceProviderModel model);
    
    @InheritConfiguration
    @Mapping(target = "publicId", qualifiedByName = "mapPublicId")
    Participant toEntity(ParticipantModel model);
    
    @InheritConfiguration
    ParticipantModel toModel(Participant participant);
    
    @InheritConfiguration
    @Mapping(target = "publicId", qualifiedByName = "mapPublicId")
    ParticipantInfo toEntity(ParticipantInfoModel model);
    
    @InheritConfiguration
    ParticipantInfoModel toModel(ParticipantInfo participant);
}
