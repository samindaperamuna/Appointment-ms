package ms.asp.appointment.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppointmentHistoryModel extends AppointmentModel {

    private Long appointmentId;
}
