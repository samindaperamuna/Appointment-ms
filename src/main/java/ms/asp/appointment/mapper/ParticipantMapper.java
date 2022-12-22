package ms.asp.appointment.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ms.asp.appointment.domain.Appointment;
import ms.asp.appointment.domain.Participant;
import ms.asp.appointment.domain.ParticipantInfo;
import ms.asp.appointment.model.participant.AppointmentModel;
import ms.asp.appointment.model.participant.ParticipantInfoModel;
import ms.asp.appointment.model.participant.ParticipantModel;

@Mapper(config = BaseMapper.class, uses = { ContactMapper.class, PeriodMapper.class })
public interface ParticipantMapper extends BaseMapper<Participant, ParticipantModel> {

    @InheritConfiguration
    ParticipantInfo toEntity(ParticipantInfoModel model);

    @InheritConfiguration
    @Mapping(target = "publicId", qualifiedByName = "mapPublicId")
    ParticipantInfoModel toModel(ParticipantInfo participantInfo);

    @InheritConfiguration
    @Mapping(source = "requestedPeriod", target = "period")
    Appointment toEntity(AppointmentModel model);

    @InheritConfiguration
    @Mapping(source = "period", target = "requestedPeriod")
    AppointmentModel toModel(Appointment appointment);
}
