package ms.asp.appointment.mapper;

import static ms.asp.appointment.util.CommonUtils.generatePublicId;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ms.asp.appointment.domain.Participant;
import ms.asp.appointment.model.ParticipantModel;

@Mapper(config = BaseMapper.class, uses = { ParticipantInfoMapper.class })
public interface ParticipantMapper extends BaseMapper<Participant, ParticipantModel> {

    @InheritConfiguration
    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "mapPublicIdParticipant")
    Participant toEntity(ParticipantModel model);

    @Named("mapPublicIdParticipant")
    default String mapPublicId(String publicId) {
	if (publicId == null || publicId.isBlank()) {
	    return generatePublicId();
	}

	return publicId;
    }
}
