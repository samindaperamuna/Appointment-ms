package ms.asp.appointment.mapper;

import static ms.asp.appointment.util.CommonUtils.generatePublicId;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ms.asp.appointment.domain.Availability;
import ms.asp.appointment.model.AvailabilityModel;

@Mapper
public interface AvailabilityMapper {

    AvailabilityModel map(Availability availability);

    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "availabilityPublicId")
    Availability map(AvailabilityModel model);

    @Named("availabilityPublicId")
    default String map(String publicId) {
	if (publicId == null || publicId.isBlank()) {
	    return generatePublicId();
	}

	return publicId;
    }
}
