package ms.asp.appointment.repository;

import org.springframework.data.r2dbc.repository.Query;

import ms.asp.appointment.domain.AvailabilityType;
import ms.asp.appointment.domain.ServiceProvider;
import reactor.core.publisher.Flux;

public interface ServiceProviderRepository extends BaseRepository<ServiceProvider, Long> {

    @Query("SELECT sp.* FROM SERVICE_PROVIDER AS sp "
	    + "INNER JOIN SERVICE_PROVIDER_AVAILABILITY AS spa ON sp.ID=spa.SERVICE_PROVIDER_ID "
	    + "INNER JOIN AVAILABILITY as a ON spa.AVAILABILITY_ID=a.ID "
	    + "WHERE a.AVAILABILITY_TYPE=:availabilityType AND a.AVAILABLE=1 ")
    Flux<ServiceProvider> findBy(AvailabilityType availabilityType);
}
