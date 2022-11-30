package ms.asp.appointment.domain;

import java.util.Set;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("APPOINTMENT_FLOW")
@EqualsAndHashCode(callSuper = true)
public class AppointmentFlow extends BaseEntity {

    private Set<ServiceType> serviceType;
    private String serviceTypeJSON;
    
    private Set<AppointmentType> appointmentType;
    private String appointmentTypeJSON;
}
