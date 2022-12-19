package ms.asp.appointment.repository;

import ms.asp.appointment.domain.Availability;
import reactor.core.publisher.Flux;

public interface AvailabilityRepository extends BaseRepository<Availability, Long> {

    Flux<Availability> findBySlotId(Long slotId);
}
