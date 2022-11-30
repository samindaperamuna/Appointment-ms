package ms.asp.appointment.domain.converter;

import org.springframework.core.convert.converter.Converter;

import ms.asp.appointment.domain.ServiceCategory;

public class SpecialityWriter implements Converter<ServiceCategory, String> {

    @Override
    public String convert(ServiceCategory source) {
	return source.getValue();
    }
}
