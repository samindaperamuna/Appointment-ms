package ms.asp.appointment.repository;

import ms.asp.appointment.domain.Appointment;
import reactor.core.publisher.Flux;

public interface AppointmentRepository extends BaseRepository<Appointment, Long> {

    Flux<Appointment> findByServiceProviderId(Long id);
}
