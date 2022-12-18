package ms.asp.appointment.repository;

import java.util.Optional;
import java.util.function.BiFunction;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import ms.asp.appointment.domain.Availability;
import ms.asp.appointment.domain.AvailabilityType;
import ms.asp.appointment.domain.Slot;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class SlotAvailabilityRepository {

    private final DatabaseClient databaseClient;

    private final BiFunction<Row, RowMetadata, Availability> MAPPING_FUNCTION = (row, rowMetaData) -> {
	Availability availability = new Availability();
	availability.setId(row.get("ID", Long.class));
	availability.setPublicId(row.get("PUBLIC_ID", String.class));
	availability.setAvailable(row.get("AVAILABLE", Boolean.class));

	Optional<AvailabilityType> optional = AvailabilityType.get(row.get("AVAILABILITY_TYPE").toString());
	optional.ifPresent(a -> availability.setAvailabilityType(a));

	return availability;
    };

    private final String INSERT_SQL = "INSERT INTO SLOT_AVAILABILITY"
	    + " (SLOT_ID, AVAILABILITY_ID)"
	    + " VALUES (:slotId, :availabilityId)";

    private final String UPDATE_SQL = "UPDATE SLOT_AVAILABILITY"
	    + " SET SLOT_ID=:slotId, AVAILABILITY_ID=:availabilityId"
	    + " WHERE (ID=:id)";

    private final String DELETE_SQL = "DELETE FROM SLOT_AVAILABILITY"
	    + " WHERE (SLOT_ID=:slotId AND AVAILABILITY_ID=:availabilityId)";

    private final String SELECT_SQL = "SELECT a.* FROM SLOT_AVAILABILITY as sa"
	    + " INNER JOIN SLOT as s ON sa.SLOT_ID=s.ID"
	    + " INNER JOIN AVAILABILITY as a ON sa.AVAILABILITY_ID=a.ID"
	    + " WHERE sa.SLOT_ID LIKE :id";

    public Mono<Long> saveSlotAvailability(Slot slot, Availability availability) {
	return databaseClient.sql(INSERT_SQL)
		.bind("slotId", slot.getId())
		.bind("availabilityId", availability.getId())
		.fetch()
		.first()
		.map(r -> (Long) r.get("ID"));
    }

    public Mono<Integer> updateSlotAvailability(Slot slot, Availability availability,
	    Long id) {
	return databaseClient.sql(UPDATE_SQL)
		.bind("slotId", slot.getId())
		.bind("availabilityId", availability.getId())
		.bind("id", id)
		.fetch()
		.rowsUpdated();
    }

    public Mono<Integer> deleteSlotAvailability(Slot slot, Availability availability) {
	return databaseClient.sql(DELETE_SQL)
		.bind("slotId", slot.getId())
		.bind("availabilityId", availability.getId())
		.fetch()
		.rowsUpdated();
    }

    public Flux<Availability> findAvailability(Slot slot, int fetchSize) {
	return databaseClient.sql(SELECT_SQL)
		.bind("id", slot.getId())
		.filter((statement, executeFunction) -> statement.fetchSize(fetchSize).execute())
		.map(MAPPING_FUNCTION)	
		.all();
    }
}
