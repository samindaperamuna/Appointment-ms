package ms.asp.appointment.domain;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppointmentType {
    DEFAULT("default");

    private final String value;

    public static Optional<AppointmentType> get(String value) {
	return Arrays.stream(AppointmentType.values())
		.filter(env -> env.value.equals(value))
		.findFirst();
    }
}
