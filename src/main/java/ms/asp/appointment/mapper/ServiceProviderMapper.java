package ms.asp.appointment.mapper;

import org.mapstruct.Mapper;

import ms.asp.appointment.domain.ServiceProvider;
import ms.asp.appointment.model.ServiceProviderModel;

@Mapper(config = BaseMapper.class, uses = { ContactMapper.class, AvailabilityMapper.class, SlotMapper.class })
public interface ServiceProviderMapper extends BaseMapper<ServiceProvider, ServiceProviderModel> {

}
