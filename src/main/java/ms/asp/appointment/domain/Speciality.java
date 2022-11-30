package ms.asp.appointment.domain;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Speciality {
    DEFAULT("default");

    private final String value;

    public static Optional<Speciality> get(String value) {
	return Arrays.stream(Speciality.values())
		.filter(env -> env.value.equals(value))
		.findFirst();
    }
}
