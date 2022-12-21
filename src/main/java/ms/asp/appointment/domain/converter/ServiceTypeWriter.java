package ms.asp.appointment.domain.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import ms.asp.appointment.domain.ServiceType;

@WritingConverter
public class ServiceTypeWriter implements Converter<ServiceType, String> {

    @Override
    public String convert(ServiceType source) {
	return source.getValue();
    }
}
