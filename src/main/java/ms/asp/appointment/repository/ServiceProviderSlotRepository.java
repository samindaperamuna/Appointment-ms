package ms.asp.appointment.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.BiFunction;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import ms.asp.appointment.domain.ServiceProvider;
import ms.asp.appointment.domain.Slot;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ServiceProviderSlotRepository {

    private final DatabaseClient databaseClient;

    private final BiFunction<Row, RowMetadata, Slot> MAPPING_FUNCTION = (row, rowMetaData) -> {
	Slot slot = new Slot();
	slot.setId(row.get("ID", Long.class));
	slot.setPublicId(row.get("PUBLIC_ID", String.class));
	slot.setStatus(row.get("STATUS", String.class));
	slot.setStart(row.get("START", LocalDateTime.class));
	slot.setEnd(row.get("END", LocalDateTime.class));
	slot.setOverbooked(row.get("OVERBOOKED", Boolean.class));
	slot.setScheduleId(row.get("SCHEDULE_ID", Long.class));

	return slot;
    };

    private final String PROVIDER_SLOT_INSERT_SQL = "INSERT INTO SERVICE_PROVIDER_SLOT"
	    + " (SERVICE_PROVIDER_ID, SLOT_ID)"
	    + " VALUES (:providerId, :slotId)";

    private final String PROVIDER_SLOT_UPDATE_SQL = "UPDATE SERVICE_PROVIDER_SLOT"
	    + " SET SERVICE_PROVIDER_ID=:providerId, SLOT_ID=:slotId"
	    + " WHERE (ID=:id)";

    private final String PROVIDER_SLOT_DELETE_SQL = "DELETE FROM SERVICE_PROVIDER_SLOT"
	    + " WHERE SERVICE_PROVIDER_ID=:providerId AND SLOT_ID=:slotId";

    private final String AM_SLOT_SQL = "SELECT s.* FROM SERVICE_PROVIDER_SLOT as sps"
	    + " INNER JOIN SERVICE_PROVIDER as sp ON sps.SERVICE_PROVIDER_ID = sp.ID"
	    + " INNER JOIN SLOT as s ON sps.SLOT_ID = s.ID"
	    + " WHERE sps.SERVICE_PROVIDER_ID LIKE :id"
	    + " AND s.END BETWEEN :startTime AND DATE_ADD(:startTime, INTERVAL 12 HOUR)";

    public Mono<Long> saveServiceProviderSlot(ServiceProvider provider, Slot slot) {
	return databaseClient.sql(PROVIDER_SLOT_INSERT_SQL)
		.bind("providerId", provider.getId())
		.bind("slotId", slot.getId())
		.fetch()
		.first()
		.map(r -> (Long) r.get("ID"));
    }

    public Mono<Integer> updateServiceProviderSlot(ServiceProvider provider, Slot slot) {
	return databaseClient.sql(PROVIDER_SLOT_UPDATE_SQL)
		.bind("providerId", provider.getId())
		.bind("slotId", slot.getId())
		.fetch()
		.rowsUpdated();
    }

    public Mono<Integer> deleteServiceProviderSlot(ServiceProvider provider, Slot slot) {
	return databaseClient.sql(PROVIDER_SLOT_DELETE_SQL)
		.bind("providerId", provider.getId())
		.bind("slotIdd", slot.getId())
		.fetch()
		.rowsUpdated();
    }

    public Flux<Slot> findAMSlots(ServiceProvider provider, LocalDate date, int fetchSize) {
	LocalDateTime dateTime = date.atTime(LocalTime.MIDNIGHT);

	return findSlot(provider, dateTime, fetchSize);
    }

    public Flux<Slot> findPMSlots(ServiceProvider provider, LocalDate date, int fetchSize) {
	LocalDateTime dateTime = date.atTime(LocalTime.NOON);

	return findSlot(provider, dateTime, fetchSize);
    }

    /**
     * Fetch the slots between 12 hour intervals for a given date.
     * 
     * @param provider  {@link ServiceProvider}
     * @param dateTime  {@link LocalDateTime}
     * @param fetchSize Max no of results
     * @return
     */
    private Flux<Slot> findSlot(ServiceProvider provider, LocalDateTime dateTime, int fetchSize) {
	return databaseClient.sql(AM_SLOT_SQL)
		.bind("id", provider.getId())
		.bind("startTime", dateTime)
		.filter((statement, executeFunction) -> statement.fetchSize(fetchSize).execute())
		.map(MAPPING_FUNCTION)
		.all();
    }
}
