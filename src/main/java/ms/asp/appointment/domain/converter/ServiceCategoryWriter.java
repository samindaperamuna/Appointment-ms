package ms.asp.appointment.domain.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import ms.asp.appointment.domain.ServiceCategory;

@WritingConverter
public class ServiceCategoryWriter implements Converter<ServiceCategory, String> {

    @Override
    public String convert(ServiceCategory source) {
	return source.getValue();
    }
}
