package ms.asp.appointment.domain;

import java.util.Set;

import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("APPOINTMENT_FLOW")
@EqualsAndHashCode(callSuper = true)
public class AppointmentFlow extends AuditedEntity {

    private String description;

    private AppointmentType appointmentType;

    @Transient
    private Set<ServiceType> serviceTypes;
    private String serviceTypeJSON;
}
