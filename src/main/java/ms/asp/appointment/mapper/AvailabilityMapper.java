package ms.asp.appointment.mapper;

import static ms.asp.appointment.util.CommonUtils.generatePublicId;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ms.asp.appointment.domain.Availability;
import ms.asp.appointment.model.AvailabilityModel;

@Mapper(config = BaseMapper.class)
public interface AvailabilityMapper extends BaseMapper<Availability, AvailabilityModel> {

    @InheritConfiguration
    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "mapPublicIdAvailability")
    Availability toEntity(AvailabilityModel model);

    @Named("mapPublicIdAvailability")
    default String mapPublicId(String publicId) {
	if (publicId == null || publicId.isBlank()) {
	    return generatePublicId();
	}

	return publicId;
    }
}
