package ms.asp.appointment.domain.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import ms.asp.appointment.domain.AppointmentType;

@WritingConverter
public class AppointmentTypeWriter implements Converter<AppointmentType, String> {

    @Override
    public String convert(AppointmentType source) {
	return source.getValue();
    }
}
