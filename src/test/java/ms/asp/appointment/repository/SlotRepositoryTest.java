package ms.asp.appointment.repository;

import static org.junit.Assert.assertNotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;

import ms.asp.appointment.domain.Slot;
import reactor.test.StepVerifier;

@DataR2dbcTest
public class SlotRepositoryTest {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    SlotRepository slotRepository;
    
    @Test
    public void testRepositoryExists() {
	assertNotNull(slotRepository);
    }

    @Test
    public void testInsertAndQuery() {
	Slot slot = new Slot();
	LocalDateTime start = LocalDateTime.parse("2022-11-30 10:30", formatter);
	LocalDateTime end = LocalDateTime.parse("2022-11-30 11:00", formatter);
	slot.setStart(start);
	slot.setEnd(end);
	slot.setComment("This is a detached slot");
	
	this.slotRepository.save(slot)
		.flatMap(s -> slotRepository.findById(s.getId()))
		.take(Duration.ofSeconds(1))
		.as(StepVerifier::create)
		.consumeNextWith(p -> assertNotNull(p))
		.verifyComplete();
    }
}
