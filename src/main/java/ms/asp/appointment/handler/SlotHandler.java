package ms.asp.appointment.handler;

import org.springframework.stereotype.Component;

import ms.asp.appointment.domain.Slot;
import ms.asp.appointment.model.slot.SlotModel;
import ms.asp.appointment.service.SlotService;

@Component
public class SlotHandler extends AbstractHandler<Slot, Long, SlotModel, SlotService>{

    public SlotHandler(SlotService service) {
	super(service, new SlotModel());
    }
}
