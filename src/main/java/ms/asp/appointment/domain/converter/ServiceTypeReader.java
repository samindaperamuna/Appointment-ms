package ms.asp.appointment.domain.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import ms.asp.appointment.domain.ServiceType;

@ReadingConverter
public class ServiceTypeReader implements Converter<String, ServiceType>{
    
    @Override
    public ServiceType convert(String source) {
	Optional<ServiceType> enumeration = ServiceType.get(source);

	return enumeration.isEmpty() ? null : enumeration.get();
    }
}
