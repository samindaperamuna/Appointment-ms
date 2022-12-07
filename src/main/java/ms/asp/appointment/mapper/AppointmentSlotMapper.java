package ms.asp.appointment.mapper;

import static ms.asp.appointment.util.CommonUtils.generatePublicId;

import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ms.asp.appointment.domain.Slot;
import ms.asp.appointment.model.AppointmentSlotModel;

@Mapper
public interface AppointmentSlotMapper {
    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "slotPublicId")
    Slot toEntity(AppointmentSlotModel slotModel);

    AppointmentSlotModel toModel(Slot slot);

    Collection<AppointmentSlotModel> toModel(Collection<Slot> e);

    @Named("slotPublicId")
    default String map(String publicId) {
	if (publicId == null || publicId.isBlank()) {
	    return generatePublicId();
	}

	return publicId;
    }
}
