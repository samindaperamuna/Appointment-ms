package ms.asp.appointment.repository;

import static org.junit.Assert.assertNotNull;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.r2dbc.core.DatabaseClient;

import ms.asp.appointment.domain.ServiceProvider;
import ms.asp.appointment.domain.ServiceType;
import ms.asp.appointment.domain.Slot;
import ms.asp.appointment.util.JSONUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataR2dbcTest
public class ServiceProviderRepositoryTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @Autowired
    DatabaseClient databaseClient;

    @Autowired
    SlotRepository slotRepository;

    @Test
    public void testRepositoryExists() {
	assertNotNull(serviceProviderRepository);
    }

    @Test
    public void testInsertAndQuery() {
	ServiceProviderSlotRepository serviceProviderSlotRepository = new ServiceProviderSlotRepository(databaseClient);

	ServiceProvider provider = new ServiceProvider();
	provider.setSubTitle("Happy Life Clinics (pvt) Ltd");
	provider.setLocation("Sri lanka");
	provider.setPrice(12.00);

	Set<DayOfWeek> offDays = new HashSet<>();
	offDays.addAll(List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));

	provider.setOffDaysJSON(JSONUtils.objectToJSON(offDays));

	Set<ServiceType> serviceTypes = new HashSet<>();
	serviceTypes.addAll(List.of(ServiceType.GYNOCOLOGIST, ServiceType.MEDICINCE));

	provider.setServiceTypesJSON(JSONUtils.serviceTypeToJSON(serviceTypes));

	Slot slot = new Slot();
	LocalDateTime start = LocalDateTime.parse("2022-11-30 10:30", formatter);
	LocalDateTime end = LocalDateTime.parse("2022-11-30 11:00", formatter);
	slot.setStart(start);
	slot.setEnd(end);

	this.serviceProviderRepository.save(provider)
		.flatMap(p -> {
		    return slotRepository.save(slot)
			    // Save service provider / slot relationship
			    .flatMap(s -> {
				serviceProviderSlotRepository.saveServiceProviderSlot(p, s)
					.subscribe();

				return Mono.just(p);
			    });
		})
		.flatMap(s -> serviceProviderRepository.findById(s.getId()))
		.take(Duration.ofSeconds(1))
		.as(StepVerifier::create)
		.consumeNextWith(p -> {
		    assertNotNull(p);
		    assertNotNull(p.getOffDaysJSON());
		    assertNotNull(p.getServiceTypesJSON());

		    serviceProviderSlotRepository.findAMSlots(p, 10)
			    .subscribe(s -> {
				assertNotNull(s);
			    });
		})
		.verifyComplete();
    }
}
