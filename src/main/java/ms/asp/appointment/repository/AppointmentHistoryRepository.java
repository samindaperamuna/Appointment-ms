package ms.asp.appointment.repository;

import org.springframework.data.r2dbc.repository.Query;

import ms.asp.appointment.domain.AppointmentHistory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AppointmentHistoryRepository extends BaseRepository<AppointmentHistory, Long> {

    @Query("SELECT * FROM APPOINTMENT_HISTORY WHERE APPOINTMENT_ID like $id")
    Flux<AppointmentHistory> findRevisions(Mono<Long> id);
}
