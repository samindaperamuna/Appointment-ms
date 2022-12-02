package ms.asp.appointment.domain.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import ms.asp.appointment.domain.AppointmentType;

@ReadingConverter
public class AppointmentTypeReader implements Converter<String, AppointmentType> {

    @Override
    public AppointmentType convert(String source) {
	Optional<AppointmentType> enumeration = AppointmentType.get(source);

	return enumeration.isEmpty() ? null : enumeration.get();
    }
}
