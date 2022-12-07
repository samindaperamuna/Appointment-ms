package ms.asp.appointment.domain;

import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppointmentType {
    DOCTOR_PATIENT("doctor-patient"),
    BUSINESS("business");

    @JsonValue
    private final String value;

    public static Optional<AppointmentType> get(String value) {
	return Arrays.stream(AppointmentType.values())
		.filter(env -> env.value.equals(value))
		.findFirst();
    }
}
