package ms.asp.appointment.domain;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServiceCategory {
    DEFAULT("default");

    private final String value;

    public static Optional<ServiceCategory> get(String value) {
	return Arrays.stream(ServiceCategory.values())
		.filter(env -> env.value.equals(value))
		.findFirst();
    }
}
