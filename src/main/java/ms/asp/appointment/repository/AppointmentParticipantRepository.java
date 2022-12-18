package ms.asp.appointment.repository;

import java.util.function.BiFunction;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import ms.asp.appointment.domain.Appointment;
import ms.asp.appointment.domain.Participant;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class AppointmentParticipantRepository {

    private final DatabaseClient databaseClient;

    private final BiFunction<Row, RowMetadata, Participant> MAPPING_FUNCTION = (row, rowMetaData) -> {
	Participant participant = new Participant();
	participant.setId(row.get("ID", Long.class));
	participant.setPublicId(row.get("PUBLIC_ID", String.class));
	participant.setType(row.get("TYPE", String.class));
	participant.setRequired(row.get("REQUIRED", Boolean.class));
	participant.setStatus(row.get("STATUS", String.class));
	participant.setAppointmentId(row.get("APPOINTMENT_ID", Long.class));
	participant.setParticipantInfoId(row.get("PARTICIPANT_INFO_ID", Long.class));

	return participant;
    };

    private final String INSERT_SQL = "INSERT INTO APPOINTMENT_PARTICIPANT"
	    + " (APPOINTMENT_ID, PARTICIPANT_ID)"
	    + " VALUES (:appointmentId, :participantId)";

    private final String UPDATE_SQL = "UPDATE APPOINTMENT_PARTICIPANT"
	    + " SET APPOINTMENT_ID=:appointmentId, PARTICIPANT_ID=:participantId"
	    + " WHERE (ID=:id)";

    private final String DELETE_SQL = "DELETE FROM APPOINTMENT_PARTICIPANT"
	    + " WHERE (APPOINTMENT_ID=:appointmentId AND PARTICIPANT_ID=:participantId)";

    private final String SELECT_SQL = "SELECT p.* FROM APPOINTMENT_PARTICIPANT as app"
	    + " INNER JOIN APPOINTMENT as ap ON app.APPOINTMENT_ID = ap.ID"
	    + " INNER JOIN PARTICIPANT as p ON app.PARTICIPANT_ID = p.ID"
	    + " WHERE app.APPOINTMENT_ID LIKE :id";

    public Mono<Long> saveAppointmentParticipant(Appointment appointment, Participant participant) {
	return databaseClient.sql(INSERT_SQL)
		.bind("appointmentId", appointment.getId())
		.bind("participantId", participant.getId())
		.fetch()
		.first()
		.map(r -> (Long) r.get("ID"));
    }

    public Mono<Integer> updateAppointmentParticipant(Appointment appointment, Participant participant,
	    Long id) {
	return databaseClient.sql(UPDATE_SQL)
		.bind("appointmentId", appointment.getId())
		.bind("participantId", participant.getId())
		.bind("id", id)
		.fetch()
		.rowsUpdated();
    }

    public Mono<Integer> deleteAppointmentParticipant(Appointment appointment, Participant participant) {
	return databaseClient.sql(DELETE_SQL)
		.bind("appointmentId", appointment.getId())
		.bind("participantId", participant.getId())
		.fetch()
		.rowsUpdated();
    }

    public Flux<Participant> findParticipant(Appointment appointment, int fetchSize) {
	return databaseClient.sql(SELECT_SQL)
		.bind("id", appointment.getId())
		.filter((statement, executeFunction) -> statement.fetchSize(fetchSize).execute())
		.map(MAPPING_FUNCTION)
		.all();
    }
}
