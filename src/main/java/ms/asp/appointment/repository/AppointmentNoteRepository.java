package ms.asp.appointment.repository;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import ms.asp.appointment.domain.Appointment;
import ms.asp.appointment.domain.Note;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AppointmentNoteRepository {

    private final DatabaseClient databaseClient;

    private final BiFunction<Row, RowMetadata, Note> MAPPING_FUNCTION = (row, rowMetaData) -> {
	Note note = new Note();
	note.setId(row.get("ID", Long.class));
	note.setPublicId(row.get("PUBLIC_ID", String.class));
	note.setAuthor(row.get("AUTHOR", String.class));
	note.setTime(row.get("TIME", LocalDateTime.class));
	note.setText(row.get("TEXT", String.class));

	return note;
    };

    private final String INSERT_SQL = "INSERT INTO APPOINTMENT_NOTE"
	    + " (APPOINTMENT_ID, NOTE_ID)"
	    + " VALUES (:appointmentId, :noteId)";

    private final String UPDATE_SQL = "UPDATE APPOINTMENT_NOTE"
	    + " SET APPOINTMENT_ID=:appointmentId, NOTE_ID=:noteId"
	    + " WHERE (ID=:id)";

    private final String DELETE_SQL = "DELETE FROM APPOINTMENT_NOTE"
	    + " WHERE (APPOINTMENT_ID=:appointmentId AND NOTE_ID=:noteId)";

    private final String SELECT_SQL = "SELECT n.* FROM APPOINTMENT_NOTE as apn"
	    + " INNER JOIN APPOINTMENT as ap ON apn.APPOINTMENT_ID = ap.ID"
	    + " INNER JOIN NOTE as n ON apn.NOTE_ID = n.ID"
	    + " WHERE apn.APPOINTMENT_ID LIKE :id";

    public Mono<Long> saveAppointmentNote(Appointment appointment, Note note) {
	return databaseClient.sql(INSERT_SQL)
		.bind("appointmentId", appointment.getId())
		.bind("noteId", note.getId())
		.fetch()
		.first()
		.map(r -> (Long) r.get("ID"));
    }

    public Mono<Integer> updateAppointmentNote(Appointment appointment, Note note,
	    Long id) {
	return databaseClient.sql(UPDATE_SQL)
		.bind("appointmentId", appointment.getId())
		.bind("noteId", note.getId())
		.bind("id", id)
		.fetch()
		.rowsUpdated();
    }

    public Mono<Integer> deleteAppointmentNote(Appointment appointment, Note note) {
	return databaseClient.sql(DELETE_SQL)
		.bind("appointmentId", appointment.getId())
		.bind("noteId", note.getId())
		.fetch()
		.rowsUpdated();
    }

    public Flux<Note> findNotes(Appointment appointment, int fetchSize) {
	return databaseClient.sql(SELECT_SQL)
		.bind("id", appointment.getId())
		.filter((statement, executeFunction) -> statement.fetchSize(fetchSize).execute())
		.map(MAPPING_FUNCTION)
		.all();
    }
}
