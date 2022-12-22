package ms.asp.appointment.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ms.asp.appointment.domain.Participant;
import ms.asp.appointment.domain.ParticipantInfo;
import ms.asp.appointment.model.participantinfo.ParticipantInfoModel;
import ms.asp.appointment.model.participantinfo.ParticipantModel;

@Mapper(config = BaseMapper.class, uses = { ContactMapper.class })
public interface ParticipantInfoMapper extends BaseMapper<ParticipantInfo, ParticipantInfoModel> {

    @InheritConfiguration
    ParticipantModel toModel(Participant participant);

    @InheritConfiguration
    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "mapPublicId")
    Participant toEntity(ParticipantModel model);
}
