package ms.asp.appointment.domain;

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
    
    private final String value;
}
