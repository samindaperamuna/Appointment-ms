package ms.asp.appointment.domain.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import ms.asp.appointment.domain.ServiceCategory;

@ReadingConverter
public class ServiceCategoryReader implements Converter<String, ServiceCategory> {

    @Override
    public ServiceCategory convert(String source) {
	Optional<ServiceCategory> enumeration = ServiceCategory.get(source);

	return enumeration.isEmpty() ? null : enumeration.get();
    }
}
