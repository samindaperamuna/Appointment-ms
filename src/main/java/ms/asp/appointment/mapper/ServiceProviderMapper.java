package ms.asp.appointment.mapper;

import org.mapstruct.Mapper;

import ms.asp.appointment.domain.ServiceProvider;
import ms.asp.appointment.model.ServiceProviderModel;

@Mapper(uses = { ContactMapper.class, ServiceProviderSlotMapper.class, AvailabilityMapper.class })
public interface ServiceProviderMapper extends BaseMapper<ServiceProvider, ServiceProviderModel> {

}
