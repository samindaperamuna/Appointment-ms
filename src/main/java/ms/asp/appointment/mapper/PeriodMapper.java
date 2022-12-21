package ms.asp.appointment.mapper;

import static ms.asp.appointment.util.CommonUtils.generatePublicId;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ms.asp.appointment.domain.Period;
import ms.asp.appointment.model.PeriodModel;

@Mapper(config = BaseMapper.class)
public interface PeriodMapper extends BaseMapper<Period, PeriodModel> {

    @InheritConfiguration
    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "mapPublicIdPeriod")
    Period toEntity(PeriodModel model);

    @Named("mapPublicIdPeriod")
    default String mapPublicId(String publicId) {
	if (publicId == null || publicId.isBlank()) {
	    return generatePublicId();
	}

	return publicId;
    }
}
