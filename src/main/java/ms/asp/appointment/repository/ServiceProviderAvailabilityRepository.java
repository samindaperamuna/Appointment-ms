package ms.asp.appointment.repository;

import java.util.function.BiFunction;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import ms.asp.appointment.domain.Availability;
import ms.asp.appointment.domain.AvailabilityType;
import ms.asp.appointment.domain.ServiceProvider;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ServiceProviderAvailabilityRepository {

    private final DatabaseClient databaseClient;

    private final BiFunction<Row, RowMetadata, Availability> MAPPING_FUNCTION = (row, rowMetaData) -> {
	Availability availability = new Availability();
	availability.setId(row.get("ID", Long.class));
	availability.setPublicId(row.get("PUBLIC_ID", String.class));
	availability.setAvailable(row.get("IS_AVAILABLE", Boolean.class));
	availability.setAvailabilityType(row.get("AVAILABILITY_TYPE", AvailabilityType.class));

	return availability;
    };

    private final String PROVIDER_AVAILABILITY_INSERT_SQL = "INSERT INTO SERVICE_PROVIDER_AVAILABILITY"
	    + " (SERVICE_PROVIDER_ID, AVAILABILITY_ID)"
	    + " VALUES (:providerId, :availabilityId)";

    private final String PROVIDER_AVAILABILITY_UPDATE_SQL = "UPDATE SERVICE_PROVIDER_AVAILABILITY"
	    + " SET SERVICE_PROVIDER_ID=:providerId, AVAILABILITY_ID:availabilityId"
	    + " WHERE (ID=:id)";

    private final String PROVIDER_AVAILABILITY_DELETE_SQL = "DELETE FROM SERVICE_PROVIDER_AVAILABILITY"
	    + " WHERE (SERVICE_PROVIDER_ID=:providerId AND ID=:id)";

    private final String PROVIDER_AVAILABILITY_SELECT_SQL = "SELECT a.* FROM SERVICE_PROVIDER_AVAILABILITY as spa"
	    + " INNER JOIN SERVICE_PROVIDER as sp ON spa.SERVICE_PROVIDER_ID = sp.ID"
	    + " INNER JOIN AVAILABILITY as a ON sps.AVAILABILITY_ID = a.ID"
	    + " WHERE spa.SERVICE_PROVIDER_ID LIKE :id";

    public Mono<Long> saveServiceProviderAvailability(ServiceProvider provider, Availability availability) {
	return databaseClient.sql(PROVIDER_AVAILABILITY_INSERT_SQL)
		.bind("providerId", provider.getId())
		.bind("availabilityId", availability.getId())
		.fetch()
		.first()
		.map(r -> (Long) r.get("ID"));
    }

    public Mono<Integer> updateServiceProviderAvailability(ServiceProvider provider, Availability availability, Long id) {
	return databaseClient.sql(PROVIDER_AVAILABILITY_UPDATE_SQL)
		.bind("providerId", provider.getId())
		.bind("availabilityId", availability.getId())
		.bind("id", id)
		.fetch()
		.rowsUpdated();
    }

    public Mono<Integer> deleteServiceProviderAvailability(ServiceProvider provider, Long id) {
	return databaseClient.sql(PROVIDER_AVAILABILITY_DELETE_SQL)
		.bind("providerId", provider.getId())
		.bind("id", id)
		.fetch()
		.rowsUpdated();
    }

    public Flux<Availability> findAvailability(ServiceProvider provider, int fetchSize) {
	return databaseClient.sql(PROVIDER_AVAILABILITY_SELECT_SQL)
		.bind("id", provider.getId())
		.filter((statement, executeFunction) -> statement.fetchSize(fetchSize).execute())
		.map(MAPPING_FUNCTION)
		.all();
    }
}
