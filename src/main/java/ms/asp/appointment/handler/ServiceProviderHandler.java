package ms.asp.appointment.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import ms.asp.appointment.domain.AvailabilityType;
import ms.asp.appointment.domain.ServiceProvider;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.exception.ServiceProviderException;
import ms.asp.appointment.model.serviceprovider.Schedule;
import ms.asp.appointment.model.serviceprovider.ServiceProviderModel;
import ms.asp.appointment.service.ServiceProviderService;
import ms.asp.appointment.util.CommonUtils;
import reactor.core.publisher.Mono;

@Component
public class ServiceProviderHandler
	extends AbstractHandler<ServiceProvider, Long, ServiceProviderModel, ServiceProviderService> {

    public ServiceProviderHandler(ServiceProviderService service) {
	super(service, new ServiceProviderModel());
    }

    public Mono<ServerResponse> getSchedule(ServerRequest req) {
	var id = req.pathVariable(ID_TEMPLATE);

	var begin = req.queryParam("begin");
	var end = req.queryParam("end");

	if (begin.isEmpty()) {
	    return Mono.error(new NotFoundException("Query parameter 'begin' required"));
	} else if (!CommonUtils.isValidDateTime(begin.get())) {
	    return Mono.error(new NotFoundException("Query parameter 'begin' needs to be of format: "
		    + CommonUtils.DATE_TIME_FORMAT));
	} else if (end.isEmpty()) {
	    return Mono.error(new NotFoundException("Query parameter 'end' required"));
	} else if (!CommonUtils.isValidDateTime(end.get())) {
	    return Mono.error(new NotFoundException("Query parameter 'end' needs to be of format: "
		    + CommonUtils.DATE_TIME_FORMAT));
	}

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CommonUtils.DATE_TIME_FORMAT);

	return ServerResponse.ok()
		.body(service.findSchedule(id, LocalDateTime.parse(begin.get(), formatter),
			LocalDateTime.parse(end.get(), formatter)), Schedule.class)
		.onErrorResume(e -> {
		    return Mono.error(
			    new ServiceProviderException("Couldn't fetch schedule for service provider with id: " + id
				    + ": " + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> getByAvailability(ServerRequest req) {
	var type = req.pathVariable("type");

	if (AvailabilityType.get(type).isEmpty()) {
	    return Mono.error(new NotFoundException("Availability type not found"));
	}

	return ServerResponse.ok()
		.body(service.findByAvailability(AvailabilityType.get(type).get()),
			ServiceProviderModel.class)
		.onErrorResume(e -> {
		    return Mono.error(
			    new ServiceProviderException("Couldn't fetch service providers for availability type "
				    + type + ": " + e.getLocalizedMessage()));
		});
    }
}
