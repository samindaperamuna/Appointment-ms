package ms.asp.appointment.domain;

import java.util.Arrays;
import java.util.Optional;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AvailabilityType {
    VIDEO_CALL("video"),
    AUDIO_CALL("audio"),
    TEXT("text"),
    OFFLINE("offline"),
    OTHER("other");

    private final String value;

    public static Optional<AvailabilityType> get(String value) {
	return Arrays.stream(AvailabilityType.values())
		.filter(env -> env.value.equals(value))
		.findFirst();
    }
}
