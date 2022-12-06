package ms.asp.appointment.domain.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import ms.asp.appointment.domain.AvailabilityType;

@ReadingConverter
public class AvailabilityTypeReader implements Converter<String, AvailabilityType> {

    @Override
    public AvailabilityType convert(String source) {
	Optional<AvailabilityType> enumeration = AvailabilityType.get(source);

	return enumeration.isEmpty() ? null : enumeration.get();
    }
}
