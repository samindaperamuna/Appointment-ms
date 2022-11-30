package ms.asp.appointment.mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.mapstruct.Mapper;

import ms.asp.appointment.domain.ServiceProvider;
import ms.asp.appointment.model.ServiceProviderModel;

@Mapper(componentModel = "spring", uses = { CodeableMapper.class })
public interface ServiceProviderMapper extends BaseMapper<ServiceProvider, ServiceProviderModel> {

    default Instant map(LocalDateTime dateTime) {
	return dateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}
