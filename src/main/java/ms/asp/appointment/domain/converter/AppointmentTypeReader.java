package ms.asp.appointment.domain.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;

import ms.asp.appointment.domain.AppointmentType;

public class AppointmentTypeReader implements Converter<String, AppointmentType> {

    @Override
    public AppointmentType convert(String source) {
	Optional<AppointmentType> enumeration = AppointmentType.get(source);

	return enumeration.isEmpty() ? null : enumeration.get();
    }
}
