package ms.asp.appointment.repository;

import java.time.LocalDateTime;

import org.springframework.data.r2dbc.repository.Query;

import ms.asp.appointment.domain.Appointment;
import reactor.core.publisher.Flux;

public interface AppointmentRepository extends BaseRepository<Appointment, Long> {

    Flux<Appointment> findByServiceProviderId(Long id);

    @Query("SELECT a.* FROM APPOINTMENT AS a INNER JOIN PERIOD AS p ON a.PERIOD_ID=p.ID "
	    + "WHERE a.SERVICE_PROVIDER_ID=:id AND p.START>=:start AND p.END<=:end")
    Flux<Appointment> findByBetween(Long id, LocalDateTime start, LocalDateTime end);
}
