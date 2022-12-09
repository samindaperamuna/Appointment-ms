package ms.asp.appointment.handler;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import ms.asp.appointment.exception.AppointmentFlowException;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.model.AppointmentFlowModel;
import ms.asp.appointment.service.AppointmentFlowService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AppointmentFlowHandler {

    private static final String idTemplate = "publicId";

    private final AppointmentFlowService appointmentFlowService;

    public Mono<ServerResponse> all(ServerRequest req) {
	var page = req.queryParam("page");
	var size = req.queryParam("size");

	if (page.isEmpty() || !NumberUtils.isCreatable(page.get())) {
	    return Mono.error(new NotFoundException("Query parameter 'page' required"));
	} else if (size.isEmpty() || !NumberUtils.isCreatable(size.get())) {
	    return Mono.error(new NotFoundException("Query parameter 'size' required"));
	}

	PageRequest pageReq = PageRequest.of(Integer.parseInt(page.get()), Integer.parseInt(size.get()));

	return ServerResponse.ok().body(appointmentFlowService.findByPage(pageReq),
		AppointmentFlowModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentFlowException("Couldn't fetch appointment flow {pageable}: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> byPublicId(ServerRequest req) {
	var id = req.pathVariable(idTemplate);

	return ServerResponse.ok().body(appointmentFlowService.findOne(id),
		AppointmentFlowModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentFlowException("Couldn't fetch appointment flow with id: "
			    + id + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> create(ServerRequest req) {
	return req.bodyToMono(AppointmentFlowModel.class)
		.flatMap(appointment -> ServerResponse.ok().body(appointmentFlowService.create(appointment),
			AppointmentFlowModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentFlowException("Couldn't create new appointment flow: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> update(ServerRequest req) {
	return req.bodyToMono(AppointmentFlowModel.class)
		.flatMap(appointment -> ServerResponse.ok().body(appointmentFlowService.update(appointment),
			AppointmentFlowModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentFlowException("Couldn't update appointment flow: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
	var id = req.pathVariable(idTemplate);

	return ServerResponse.ok().body(appointmentFlowService.delete(id),
		AppointmentFlowModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentFlowException("Couldn't delete appointment flow with id: " + id
			    + ": " + e.getLocalizedMessage()));
		});
    }
}