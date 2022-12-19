package ms.asp.appointment.repository;

import static org.junit.Assert.assertNotNull;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.r2dbc.core.DatabaseClient;

import ms.asp.appointment.domain.ServiceProvider;
import ms.asp.appointment.domain.ServiceType;
import ms.asp.appointment.util.CommonUtils;
import ms.asp.appointment.util.JSONUtils;
import reactor.test.StepVerifier;

@DataR2dbcTest
public class ServiceProviderRepositoryTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CommonUtils.TIME_FORMAT);

    @Autowired
    ServiceProviderRepository serviceProviderRepository;

    @Autowired
    DatabaseClient databaseClient;

    @Test
    public void testRepositoryExists() {
	assertNotNull(serviceProviderRepository);
    }

    @Test
    public void testInsertAndQuery() {
	ServiceProvider provider = new ServiceProvider();
	provider.setSubTitle("Happy Life Clinics (pvt) Ltd");
	provider.setLocation("Sri lanka");
	provider.setPrice(12.00);

	Set<DayOfWeek> offDays = new HashSet<>();
	offDays.addAll(List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY));

	provider.setOffDaysJSON(JSONUtils.objectToJSON(offDays));

	List<ServiceType> serviceTypes = new ArrayList<>();
	serviceTypes.addAll(List.of(ServiceType.GYNOCOLOGIST, ServiceType.MEDICINCE));

	provider.setServiceTypesJSON(JSONUtils.serviceTypeToJSON(serviceTypes));

	this.serviceProviderRepository.save(provider)
		.flatMap(s -> serviceProviderRepository.findById(s.getId()))
		.take(Duration.ofSeconds(1))
		.as(StepVerifier::create)
		.consumeNextWith(p -> {
		    assertNotNull(p);
		    assertNotNull(p.getOffDaysJSON());
		    assertNotNull(p.getServiceTypesJSON());
		})
		.verifyComplete();
    }
}
