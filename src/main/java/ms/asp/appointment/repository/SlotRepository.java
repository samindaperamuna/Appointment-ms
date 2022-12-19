package ms.asp.appointment.repository;

import java.time.LocalTime;

import org.springframework.data.r2dbc.repository.Query;

import ms.asp.appointment.domain.Slot;
import reactor.core.publisher.Flux;

public interface SlotRepository extends BaseRepository<Slot, Long> {

    @Query("SELECT * FROM SLOT WHERE START BETWEEN :startTime AND :endTime AND SERVICE_PROVIDER_ID=:serviceProviderId")
    Flux<Slot> findByProviderAndStartBetween(Long serviceProviderId, LocalTime startTime, LocalTime endTime);
}
