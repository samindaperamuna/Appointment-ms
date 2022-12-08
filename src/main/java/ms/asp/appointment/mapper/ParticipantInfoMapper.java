package ms.asp.appointment.mapper;

import static ms.asp.appointment.util.CommonUtils.generatePublicId;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ms.asp.appointment.domain.ParticipantInfo;
import ms.asp.appointment.model.ParticipantInfoModel;

@Mapper(config = BaseMapper.class, uses = { ContactMapper.class })
public interface ParticipantInfoMapper extends BaseMapper<ParticipantInfo, ParticipantInfoModel> {

    @InheritConfiguration
    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "mapPublicIdParticipantInfo")
    ParticipantInfo toEntity(ParticipantInfoModel model);

    @Named("mapPublicIdParticipantInfo")
    default String mapPublicId(String publicId) {
	if (publicId == null || publicId.isBlank()) {
	    return generatePublicId();
	}

	return publicId;
    }
}
