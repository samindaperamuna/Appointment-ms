package ms.asp.appointment.model.serviceprovider;

import java.util.List;

import lombok.Data;
import ms.asp.appointment.domain.Period;

@Data
public class Schedule {

    private Period period;
    private List<AppointmentModel> appointments;
    private List<SlotModel> slots;
}
