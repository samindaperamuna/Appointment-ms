package ms.asp.appointment.domain;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("APPOINTMENT_HISTORY")
@EqualsAndHashCode(callSuper = true)
public class AppointmentHistory extends Appointment {

    private Long appointmentId;
    private long version;
}
