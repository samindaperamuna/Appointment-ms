package ms.asp.appointment.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import ms.asp.appointment.domain.Appointment;
import ms.asp.appointment.exception.AppointmentException;
import ms.asp.appointment.model.PeriodModel;
import ms.asp.appointment.model.appointment.AppointmentModel;
import ms.asp.appointment.service.AppointmentService;
import reactor.core.publisher.Mono;

@Component
public class AppointmentHandler extends AbstractHandler<Appointment, Long, AppointmentModel, AppointmentService> {

    @Value("${appointment.ics-filename}")
    private static String icsFileName;

    public AppointmentHandler(AppointmentService service) {
	super(service, new AppointmentModel());
    }
    
    public Mono<ServerResponse> reschedule(ServerRequest req) {
	var id = req.pathVariable(ID_TEMPLATE);

	return req.bodyToMono(PeriodModel.class)
		.flatMap(pm -> ServerResponse.ok()
			.body(service.reschedule(id, pm), AppointmentModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentException("Couldn't reschedule appointment with id:" + id
			    + ": " + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> history(ServerRequest req) {
	var id = req.pathVariable(ID_TEMPLATE);

	return ServerResponse.ok()
		.body(service.getHistory(id), AppointmentModel.class)
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
		.body(service.genCalendarFile(id), Resource.class)
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
		.body(service.getFHIRAppointment(id), String.class)
		.onErrorResume(e -> {
		    return Mono
			    .error(new AppointmentException("Couldn't generate FHIR data for appointment with id: " + id
				    + ": " + e.getLocalizedMessage()));
		});
    }
}
