package ms.asp.appointment.domain.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import ms.asp.appointment.domain.Speciality;

@ReadingConverter
public class SpecialityReader implements Converter<String, Speciality> {

    @Override
    public Speciality convert(String source) {
	Optional<Speciality> enumeration = Speciality.get(source);

	return enumeration.isEmpty() ? null : enumeration.get();
    }
}
