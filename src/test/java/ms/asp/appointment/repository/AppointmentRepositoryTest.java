package ms.asp.appointment.repository;

import static org.junit.Assert.assertNotNull;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
