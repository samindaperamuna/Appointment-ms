package ms.asp.appointment.handler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import ms.asp.appointment.domain.AvailabilityType;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.exception.ServiceProviderException;
import ms.asp.appointment.model.Schedule;
import ms.asp.appointment.model.ServiceProviderModel;
import ms.asp.appointment.service.ServiceProviderService;
import ms.asp.appointment.util.CommonUtils;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ServiceProviderHandler {

    private static final String idTemplate = "publicId";

    private final ServiceProviderService serviceProviderService;

    public Mono<ServerResponse> all(ServerRequest req) {
	var page = req.queryParam("page");
	var size = req.queryParam("size");

	if (page.isEmpty() || !NumberUtils.isCreatable(page.get())) {
	    return Mono.error(new NotFoundException("Query parameter 'page' required"));
	} else if (size.isEmpty() || !NumberUtils.isCreatable(size.get())) {
	    return Mono.error(new NotFoundException("Query parameter 'size' required"));
	}

	PageRequest pageReq = PageRequest.of(Integer.parseInt(page.get()), Integer.parseInt(size.get()));

	return ServerResponse.ok().body(serviceProviderService.findByPage(pageReq),
		ServiceProviderModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new ServiceProviderException("Couldn't fetch service providers {pageable}: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> byPublicId(ServerRequest req) {
	var id = req.pathVariable(idTemplate);

	return ServerResponse.ok().body(serviceProviderService.findOne(id),
		ServiceProviderModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new ServiceProviderException("Couldn't fetch service provider with id: "
			    + id + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> create(ServerRequest req) {
	return req.bodyToMono(ServiceProviderModel.class)
		.flatMap(appointment -> ServerResponse.ok().body(serviceProviderService.create(appointment),
			ServiceProviderModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new ServiceProviderException("Couldn't create new service provider: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> update(ServerRequest req) {
	return req.bodyToMono(ServiceProviderModel.class)
		.flatMap(appointment -> ServerResponse.ok().body(serviceProviderService.update(appointment),
			ServiceProviderModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new ServiceProviderException("Couldn't update service provider: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
	var id = req.pathVariable(idTemplate);

	return ServerResponse.ok().body(serviceProviderService.delete(id),
		ServiceProviderModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new ServiceProviderException("Couldn't delete service provider with id: " + id
			    + ": " + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> getSchedule(ServerRequest req) {
	var id = req.pathVariable(idTemplate);

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
		.body(serviceProviderService.findSchedule(id, LocalDateTime.parse(begin.get(), formatter),
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
		.body(serviceProviderService.findByAvailability(AvailabilityType.get(type).get()),
			ServiceProviderModel.class)
		.onErrorResume(e -> {
		    return Mono.error(
			    new ServiceProviderException("Couldn't fetch service providers for availability type "
				    + type + ": " + e.getLocalizedMessage()));
		});
    }
}
