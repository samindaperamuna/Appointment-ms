package ms.asp.appointment.domain;

import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Speciality {
    DEFAULT("default");

    @JsonValue
    private final String value;

    public static Optional<Speciality> get(String value) {
	return Arrays.stream(Speciality.values())
		.filter(env -> env.value.equals(value))
		.findFirst();
    }
}
