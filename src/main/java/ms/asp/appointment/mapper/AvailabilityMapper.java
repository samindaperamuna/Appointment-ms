package ms.asp.appointment.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ms.asp.appointment.domain.Availability;
import ms.asp.appointment.domain.ServiceProvider;
import ms.asp.appointment.domain.Slot;
import ms.asp.appointment.model.availability.AvailabilityModel;
import ms.asp.appointment.model.availability.ServiceProviderModel;
import ms.asp.appointment.model.availability.SlotModel;

@Mapper(config = BaseMapper.class)
public interface AvailabilityMapper extends BaseMapper<Availability, AvailabilityModel> {

    @InheritConfiguration
    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "mapPublicId")
    Slot toEntity(SlotModel model);

    @InheritConfiguration
    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "mapPublicId")
    ServiceProvider toEntity(ServiceProviderModel model);
}
