package ms.asp.appointment.mapper;

import static ms.asp.appointment.util.CommonUtils.generatePublicId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ms.asp.appointment.domain.Slot;
import ms.asp.appointment.model.ServiceProviderSlotModel;

@Mapper(imports = { LocalDateTime.class, LocalDate.class })
public interface ServiceProviderSlotMapper {

    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "slotPublicId")
    @Mapping(target = "start", expression = "java(LocalDateTime.of(LocalDate.now(), slotModel.getStart()))")
    @Mapping(target = "end", expression = "java(LocalDateTime.of(LocalDate.now(), slotModel.getEnd()))")
    Slot toEntity(ServiceProviderSlotModel slotModel);

    @Mapping(target = "start", expression = "java(slot.getStart().toLocalTime())")
    @Mapping(target = "end", expression = "java(slot.getEnd().toLocalTime())")
    ServiceProviderSlotModel toModel(Slot slot);

    Collection<ServiceProviderSlotModel> toModel(Collection<Slot> e);

    @Named("slotPublicId")
    default String map(String publicId) {
	if (publicId == null || publicId.isBlank()) {
	    return generatePublicId();
	}

	return publicId;
    }
}
