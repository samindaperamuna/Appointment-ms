package ms.asp.appointment.repository;

import static org.junit.Assert.assertNotNull;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ms.asp.appointment.domain.Appointment;
import ms.asp.appointment.domain.AppointmentType;
import reactor.test.StepVerifier;

@SpringBootTest
public class AppointmentRepositoryTest {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Test
    public void testRepositoryExists() {
	assertNotNull(appointmentRepository);
    }

    @Test
    public void testInsertAndQuery() {
	Appointment appointment = new Appointment();
	appointment.setAppointmentType(AppointmentType.DEFAULT);
	appointment.setDescription("Test Appointment");
	appointment.setMinutesDuration(30);

	this.appointmentRepository.save(appointment)
		.flatMap(s -> appointmentRepository.findById(s.getId()))
		.take(Duration.ofSeconds(1))
		.as(StepVerifier::create)
		.consumeNextWith(p -> assertNotNull(p))
		.verifyComplete();
    }
}
