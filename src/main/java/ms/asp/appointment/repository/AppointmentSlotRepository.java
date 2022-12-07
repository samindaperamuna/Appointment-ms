package ms.asp.appointment.repository;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import ms.asp.appointment.domain.Appointment;
import ms.asp.appointment.domain.Slot;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AppointmentSlotRepository {

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

    private final String INSERT_SQL = "INSERT INTO APPOINTMENT_SLOT"
	    + " (APPOINTMENT_ID, SLOT_ID)"
	    + " VALUES (:appointmentId, :slotId)";

    private final String UPDATE_SQL = "UPDATE APPOINTMENT_SLOT"
	    + " SET APPOINTMENT_ID=:appointmentId, SLOT_ID=:slotId"
	    + " WHERE (ID=:id)";

    private final String DELETE_SQL = "DELETE FROM APPOINTMENT_SLOT"
	    + " WHERE APPOINTMENT_ID=:appointmentId AND SLOT_ID=:slotId";

    private final String SELECT_SQL = "SELECT s.* FROM APPOINTMENT_SLOT as aps"
	    + " INNER JOIN APPOINTMENT as a ON aps.APPOINTMENT_ID = a.ID"
	    + " INNER JOIN SLOT as s ON aps.SLOT_ID = s.ID"
	    + " WHERE aps.APPOINTMENT_ID LIKE :id";

    public Mono<Long> saveAppointmentSlot(Appointment provider, Slot slot) {
	return databaseClient.sql(INSERT_SQL)
		.bind("appointmentId", provider.getId())
		.bind("slotId", slot.getId())
		.fetch()
		.first()
		.map(r -> (Long) r.get("ID"));
    }

    public Mono<Integer> updateAppointmentSlot(Appointment appointment, Slot slot) {
	return databaseClient.sql(UPDATE_SQL)
		.bind("appointmentId", appointment.getId())
		.bind("slotId", slot.getId())
		.fetch()
		.rowsUpdated();
    }

    public Mono<Integer> deleteAppointmentSlot(Appointment appointment, Slot slot) {
	return databaseClient.sql(DELETE_SQL)
		.bind("appointmentId", appointment.getId())
		.bind("slotIdd", slot.getId())
		.fetch()
		.rowsUpdated();
    }

    public Flux<Slot> findSlots(Appointment appointment, int fetchSize) {
	return databaseClient.sql(SELECT_SQL)
		.bind("id", appointment.getId())
		.filter((statement, executeFunction) -> statement.fetchSize(fetchSize).execute())
		.map(MAPPING_FUNCTION)
		.all();
    }
}
