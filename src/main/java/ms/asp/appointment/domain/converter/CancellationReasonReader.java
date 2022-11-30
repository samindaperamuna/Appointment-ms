package ms.asp.appointment.domain.converter;

import java.util.Optional;

import org.springframework.core.convert.converter.Converter;

import ms.asp.appointment.domain.CancellationReason;

public class CancellationReasonReader implements Converter<String, CancellationReason> {

    @Override
    public CancellationReason convert(String source) {
	Optional<CancellationReason> enumeration = CancellationReason.get(source);

	return enumeration.isEmpty() ? null : enumeration.get();
    }
}
