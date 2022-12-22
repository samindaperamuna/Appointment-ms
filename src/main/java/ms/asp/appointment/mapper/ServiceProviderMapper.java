package ms.asp.appointment.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ms.asp.appointment.domain.Appointment;
import ms.asp.appointment.domain.Availability;
import ms.asp.appointment.domain.ServiceProvider;
import ms.asp.appointment.domain.Slot;
import ms.asp.appointment.model.serviceprovider.AppointmentModel;
import ms.asp.appointment.model.serviceprovider.AvailabilityModel;
import ms.asp.appointment.model.serviceprovider.ServiceProviderModel;
import ms.asp.appointment.model.serviceprovider.SlotModel;

@Mapper(config = BaseMapper.class, uses = { ContactMapper.class })
public interface ServiceProviderMapper extends BaseMapper<ServiceProvider, ServiceProviderModel> {

    @InheritConfiguration
    AppointmentModel toModel(Appointment appointment);

    @InheritConfiguration
    SlotModel toModel(Slot slot);

    @InheritConfiguration
    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "mapPublicId")
    Slot toEntity(SlotModel model);

    @InheritConfiguration
    AvailabilityModel toModel(Availability availability);
    
    @InheritConfiguration
    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "mapPublicId")
    Availability toEntity(AvailabilityModel model);
}
