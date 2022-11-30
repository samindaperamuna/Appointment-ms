package ms.asp.appointment.domain.converter;

import org.springframework.core.convert.converter.Converter;

import ms.asp.appointment.domain.ServiceCategory;

public class CancellationReasonWriter implements Converter<ServiceCategory, String> {

    @Override
    public String convert(ServiceCategory source) {
	return source.getValue();
    }
}
