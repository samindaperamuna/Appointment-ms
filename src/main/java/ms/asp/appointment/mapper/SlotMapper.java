package ms.asp.appointment.mapper;

import static ms.asp.appointment.util.CommonUtils.generatePublicId;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ms.asp.appointment.domain.Slot;
import ms.asp.appointment.model.SlotModel;

@Mapper(config = BaseMapper.class, uses = { AvailabilityMapper.class })
public interface SlotMapper extends BaseMapper<Slot, SlotModel> {

    @InheritConfiguration
    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "mapPublicIdSlot")
    Slot toEntity(SlotModel model);

    @Named("mapPublicIdSlot")
    default String mapPublicId(String publicId) {
	if (publicId == null || publicId.isBlank()) {
	    return generatePublicId();
	}

	return publicId;
    }
}
