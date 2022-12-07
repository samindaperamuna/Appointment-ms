package ms.asp.appointment.repository;

import org.springframework.data.r2dbc.repository.Query;

import ms.asp.appointment.domain.AppointmentHistory;
import reactor.core.publisher.Flux;

public interface AppointmentHistoryRepository extends BaseRepository<AppointmentHistory, Long> {

    Flux<AppointmentHistory> findByAppointmentId(Long id);

    @Query("SELECT * FROM APPOINTMENT_HISTORY WHERE PUBLIC_ID=:publicId")
    Flux<AppointmentHistory> findRevisions(String publicId);
}
