package ms.asp.appointment.domain.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import ms.asp.appointment.domain.Speciality;

@WritingConverter
public class SpecialityWriter implements Converter<Speciality, String> {

    @Override
    public String convert(Speciality source) {
	return source.getValue();
    }
}
