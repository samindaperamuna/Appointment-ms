package ms.asp.appointment.domain.listeners;

import lombok.extern.slf4j.Slf4j;
import ms.asp.appointment.domain.Appointment;

@Slf4j
@SuppressWarnings(value = "all")
public class FHIRListener {

    private void afterAnyUpdate(Appointment appointement) {
	log.info("complete for appointement: " + appointement.getPublicId());
	// TODO: Add FHIR client here for replication related work.
    }
}
