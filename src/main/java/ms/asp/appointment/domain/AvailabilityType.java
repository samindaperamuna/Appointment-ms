package ms.asp.appointment.domain;

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
}
