package ms.asp.appointment.handler;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import ms.asp.appointment.exception.AppointmentException;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.model.AppointmentModel;
import ms.asp.appointment.model.PeriodModel;
import ms.asp.appointment.service.AppointmentService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AppointmentHandler {

    private static final String ID_TEMPLATE = "publicId";

    @Value("${appointment.ics-filename}")
    private static String icsFileName;

    private final AppointmentService appointmentService;

    public Mono<ServerResponse> all(ServerRequest req) {
	var page = req.queryParam("page");
	var size = req.queryParam("size");

	if (page.isEmpty() || !NumberUtils.isCreatable(page.get())) {
	    return Mono.error(new NotFoundException("Query parameter 'page' required"));
	} else if (size.isEmpty() || !NumberUtils.isCreatable(size.get())) {
	    return Mono.error(new NotFoundException("Query parameter 'size' required"));
	}

	PageRequest pageReq = PageRequest.of(Integer.parseInt(page.get()), Integer.parseInt(size.get()));

	return ServerResponse.ok()
		.body(appointmentService.findByPage(pageReq), AppointmentModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentException("Couldn't fetch appointments {pageable}: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> byPublicId(ServerRequest req) {
	var id = req.pathVariable(ID_TEMPLATE);

	return ServerResponse.ok()
		.body(appointmentService.findOne(id), AppointmentModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentException("Couldn't fetch appointment with id: " + id
			    + ": " + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> create(ServerRequest req) {
	return req.bodyToMono(AppointmentModel.class)
		.flatMap(appointment -> ServerResponse.ok()
			.body(appointmentService.create(appointment), AppointmentModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentException("Couldn't create new appointment: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> update(ServerRequest req) {
	return req.bodyToMono(AppointmentModel.class)
		.flatMap(appointment -> ServerResponse.ok()
			.body(appointmentService.update(appointment), AppointmentModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentException("Couldn't update appointment: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
	var id = req.pathVariable(ID_TEMPLATE);

	return ServerResponse.ok()
		.body(appointmentService.delete(id), AppointmentModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentException("Couldn't delete appointment with id: " + id
			    + ": " + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> reschedule(ServerRequest req) {
	var id = req.pathVariable(ID_TEMPLATE);

	return req.bodyToMono(PeriodModel.class)
		.flatMap(pm -> ServerResponse.ok()
			.body(appointmentService.reschedule(id, pm), AppointmentModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentException("Couldn't reschedule appointment with id:" + id
			    + ": " + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> history(ServerRequest req) {
	var id = req.pathVariable(ID_TEMPLATE);

	return ServerResponse.ok()
		.body(appointmentService.getHistory(id), AppointmentModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentException("Couldn't get history for appointment with id: " + id
			    + ": " + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> calendar(ServerRequest req) {
	var id = req.pathVariable(ID_TEMPLATE);

	return ServerResponse.ok()
		.headers(headers -> {
		    headers.add(HttpHeaders.CONTENT_DISPOSITION,
			    "attachment; filename=" + icsFileName + ".ics");
		    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		    headers.add("Pragma", "no-cache");
		    headers.add("Expires", "0");
		})
		.contentType(MediaType.APPLICATION_OCTET_STREAM)
		.body(appointmentService.genCalendarFile(id), Resource.class)
		.onErrorResume(e -> {
		    return Mono
			    .error(new AppointmentException("Couldn't generate calendar for appointment with id: " + id
				    + ": " + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> fhir(ServerRequest req) {
	var id = req.pathVariable(ID_TEMPLATE);

	return ServerResponse.ok()
		.contentType(MediaType.APPLICATION_JSON)
		.body(appointmentService.getFHIRAppointment(id), String.class)
		.onErrorResume(e -> {
		    return Mono
			    .error(new AppointmentException("Couldn't generate FHIR data for appointment with id: " + id
				    + ": " + e.getLocalizedMessage()));
		});
    }
}
