package ms.asp.appointment.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import ms.asp.appointment.annotation.doc.AppointmentDoc;
import ms.asp.appointment.annotation.doc.AppointmentFlowDoc;
import ms.asp.appointment.annotation.doc.AvailabilityDoc;
import ms.asp.appointment.annotation.doc.ContactDoc;
import ms.asp.appointment.annotation.doc.ParticipantDoc;
import ms.asp.appointment.annotation.doc.ParticipantInfoDoc;
import ms.asp.appointment.annotation.doc.ServiceProviderDoc;
import ms.asp.appointment.annotation.doc.SlotDoc;
import ms.asp.appointment.handler.AppointmentFlowHandler;
import ms.asp.appointment.handler.AppointmentHandler;
import ms.asp.appointment.handler.AvailabilityHandler;
import ms.asp.appointment.handler.ContactHandler;
import ms.asp.appointment.handler.ParticipantHandler;
import ms.asp.appointment.handler.ParticipantInfoHandler;
import ms.asp.appointment.handler.ServiceProviderHandler;
import ms.asp.appointment.handler.SlotHandler;

@Configuration
@RequiredArgsConstructor
public class RouterConfig {

    private final AppointmentHandler appointmentHandler;
    private final ParticipantHandler participantHandler;
    private final ParticipantInfoHandler participantInfoHandler;
    private final ContactHandler contactHandler;
    private final ServiceProviderHandler serviceProviderHandler;
    private final SlotHandler slotHandler;
    private final AvailabilityHandler availabilityHandler;
    private final AppointmentFlowHandler appointmentFlowHandler;

    @Bean
    @AppointmentDoc
    public RouterFunction<ServerResponse> appointmentRoutes() {
	return RouterFunctions
		.route(GET("/appointments"), appointmentHandler::all)
		.andRoute(GET("/appointments/{publicId}"), appointmentHandler::byPublicId)
		.andRoute(POST("/appointments"), appointmentHandler::create)
		.andRoute(PUT("/appointments"), appointmentHandler::update)
		.andRoute(DELETE("/appointments/{publicId}"), appointmentHandler::delete)
		.andRoute(PUT("/appointments/{publicId}/reschedule"), appointmentHandler::reschedule)
		.andRoute(GET("/appointments/{publicId}/history"), appointmentHandler::history)
		.andRoute(GET("/appointments/{publicId}/calendar"), appointmentHandler::calendar)
		.andRoute(GET("/appointments/{publicId}/fhir"), appointmentHandler::fhir);
    }

    @Bean
    @ParticipantDoc
    public RouterFunction<ServerResponse> participantRoutes() {
	return RouterFunctions
		.route(GET("/participants/{publicId}"), participantHandler::byPublicId)
		.andRoute(POST("/participants"), participantHandler::create)
		.andRoute(PUT("/participants"), participantHandler::update)
		.andRoute(DELETE("/participants/{publicId}"), participantHandler::delete);
    }

    @Bean
    @ParticipantInfoDoc
    public RouterFunction<ServerResponse> participantInfoRoutes() {
	return RouterFunctions
		.route(GET("/participantinfo/{publicId}"), participantInfoHandler::byPublicId)
		.andRoute(POST("/participantinfo"), participantInfoHandler::create)
		.andRoute(PUT("/participantinfo"), participantInfoHandler::update)
		.andRoute(DELETE("/participantinfo/{publicId}"), participantInfoHandler::delete);
    }

    @Bean
    @ContactDoc
    public RouterFunction<ServerResponse> contactRoutes() {
	return RouterFunctions
		.route(GET("/contacts/{publicId}"), contactHandler::byPublicId)
		.andRoute(POST("/contacts"), contactHandler::create)
		.andRoute(PUT("/contacts"), contactHandler::update)
		.andRoute(DELETE("/contacts/{publicId}"), contactHandler::delete);
    }

    @Bean
    @ServiceProviderDoc
    public RouterFunction<ServerResponse> serviceProviderRoutes() {
	return RouterFunctions
		.route(GET("/serviceproviders"), serviceProviderHandler::all)
		.andRoute(GET("/serviceproviders/{publicId}"), serviceProviderHandler::byPublicId)
		.andRoute(POST("/serviceproviders"), serviceProviderHandler::create)
		.andRoute(PUT("/serviceproviders"), serviceProviderHandler::update)
		.andRoute(DELETE("/serviceproviders/{publicId}"), serviceProviderHandler::delete)
		.andRoute(GET("/serviceproviders/{publicId}/schedule"), serviceProviderHandler::getSchedule)
		.andRoute(GET("/serviceproviders/availability/{type}"), serviceProviderHandler::getByAvailability);
    }

    @Bean
    @SlotDoc
    public RouterFunction<ServerResponse> slotRoutes() {
	return RouterFunctions
		.route(GET("/slots/{publicId}"), slotHandler::byPublicId)
		.andRoute(POST("/slots"), slotHandler::create)
		.andRoute(PUT("/slots"), slotHandler::update)
		.andRoute(DELETE("/slots/{publicId}"), slotHandler::delete);
    }

    @Bean
    @AvailabilityDoc
    public RouterFunction<ServerResponse> availabilityRoutes() {
	return RouterFunctions
		.route(GET("/availability/{publicId}"), availabilityHandler::byPublicId)
		.andRoute(POST("/availability"), availabilityHandler::create)
		.andRoute(PUT("/availability"), availabilityHandler::update)
		.andRoute(DELETE("/availability/{publicId}"), availabilityHandler::delete);
    }

    @Bean
    @AppointmentFlowDoc
    public RouterFunction<ServerResponse> appointmentFlowRoutes() {
	return RouterFunctions
		.route(GET("/appointmentflows"), appointmentFlowHandler::all)
		.andRoute(GET("/appointmentflows/{publicId}"), appointmentFlowHandler::byPublicId)
		.andRoute(POST("/appointmentflows"), appointmentFlowHandler::create)
		.andRoute(PUT("/appointmentflows"), appointmentFlowHandler::update)
		.andRoute(DELETE("/appointmentflows/{publicId}"), appointmentFlowHandler::delete);
    }
}
