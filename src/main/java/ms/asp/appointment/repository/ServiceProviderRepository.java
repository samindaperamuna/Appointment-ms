package ms.asp.appointment.repository;

import org.springframework.data.r2dbc.repository.Query;

import ms.asp.appointment.domain.AvailabilityType;
import ms.asp.appointment.domain.ServiceProvider;
import reactor.core.publisher.Flux;

public interface ServiceProviderRepository extends BaseRepository<ServiceProvider, Long> {

    @Query("SELECT sp.* FROM SERVICE_PROVIDER AS sp "
    	+ "INNER JOIN SLOT AS s ON sp.ID=s.SERVICE_PROVIDER_ID "
    	+ "INNER JOIN AVAILABILITY AS a ON s.ID=a.SLOT_ID "
    	+ "WHERE a.AVAILABILITY_TYPE=:availabilityType AND a.AVAILABLE=1 "
    	+ "GROUP BY sp.ID ")
    Flux<ServiceProvider> findBy(AvailabilityType availabilityType);
}
