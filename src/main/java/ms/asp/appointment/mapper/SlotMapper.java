package ms.asp.appointment.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ms.asp.appointment.domain.Availability;
import ms.asp.appointment.domain.ServiceProvider;
import ms.asp.appointment.domain.Slot;
import ms.asp.appointment.model.slot.AvailabilityModel;
import ms.asp.appointment.model.slot.ServiceProviderModel;
import ms.asp.appointment.model.slot.SlotModel;

@Mapper(config = BaseMapper.class)
public interface SlotMapper extends BaseMapper<Slot, SlotModel> {

    @InheritConfiguration
    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "mapPublicId")
    ServiceProvider toEntity(ServiceProviderModel model);
    
    @InheritConfiguration
    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "mapPublicId")
    Availability toEntity(AvailabilityModel model);
}
