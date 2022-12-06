package ms.asp.appointment.domain.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import ms.asp.appointment.domain.AvailabilityType;

@WritingConverter
public class AvailabilityTypeWriter implements Converter<AvailabilityType, String> {

    @Override
    public String convert(AvailabilityType source) {
	return source.getValue();
    }
}
