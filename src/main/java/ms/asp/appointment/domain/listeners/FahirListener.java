package ms.asp.appointment.domain.listeners;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;
import ms.asp.appointment.domain.Appointement;

@Slf4j
public class FahirListener {
  @PostPersist
  @PostUpdate
  @PostRemove
  private void afterAnyUpdate(Appointement appointement) {
    log.info("complete for appointement: " + appointement.getPublicId());
    // TODO: add fhir client here for replication related work
  }
}
