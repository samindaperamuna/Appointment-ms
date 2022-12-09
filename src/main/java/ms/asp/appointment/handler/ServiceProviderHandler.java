package ms.asp.appointment.handler;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import ms.asp.appointment.exception.ServiceProviderException;
import ms.asp.appointment.model.ServiceProviderModel;
import ms.asp.appointment.service.ServiceProviderService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ServiceProviderHandler {

    private static final String idTemplate = "publicId";

    private final ServiceProviderService serviceProviderService;

    public Mono<ServerResponse> all(ServerRequest req) {
	return req.bodyToMono(Pageable.class)
		.flatMap(pageable -> ServerResponse.ok().body(serviceProviderService.findAll(pageable),
			ServiceProviderModel.class))
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

	return ServerResponse.ok().body(null)
		.onErrorResume(e -> {
		    return Mono.error(
			    new ServiceProviderException("Couldn't fetch schedule for service provider with id: " + id
				    + ": " + e.getLocalizedMessage()));
		});
    }
}
