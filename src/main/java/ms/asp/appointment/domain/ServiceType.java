package ms.asp.appointment.domain;

import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServiceType {

    // Engineering / IT
    CIVIL_ENGINEER("civil-eng"),
    MECHANICAL_ENGINEER("mech-eng"),
    SOFTWARE_DEVELOPER("soft-dev"),

    // Health
    MEDICINCE("med"),
    GYNOCOLOGIST("gyno");

    @JsonValue
    private final String value;

    public static Optional<ServiceType> get(String value) {
	return Arrays.stream(ServiceType.values())
		.filter(env -> env.value.equals(value))
		.findFirst();
    }
}
