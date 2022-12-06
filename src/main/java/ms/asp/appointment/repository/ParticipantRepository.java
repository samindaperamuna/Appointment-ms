package ms.asp.appointment.repository;

import ms.asp.appointment.domain.Participant;
import reactor.core.publisher.Flux;

public interface ParticipantRepository extends BaseRepository<Participant, Long> {

    Flux<Participant> findByAppointmentId(Long id);
}
