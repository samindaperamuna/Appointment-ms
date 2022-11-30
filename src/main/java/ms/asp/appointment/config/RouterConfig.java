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
import ms.asp.appointment.handler.AppointmentHandler;
import ms.asp.appointment.handler.ServiceProviderHandler;

@Configuration
@RequiredArgsConstructor
public class RouterConfig {

    private final AppointmentHandler appointmentHandler;
    private final ServiceProviderHandler serviceProviderHandler;

    @Bean
    public RouterFunction<ServerResponse> routes() {
	return RouterFunctions
		// Appointments
		.route(GET("/appointments"), appointmentHandler::all)
		.andRoute(GET("/appointments/{publicId}"), appointmentHandler::byPublicId)
		.andRoute(POST("/appointments"), appointmentHandler::create)
		.andRoute(PUT("/appointments"), appointmentHandler::update)
		.andRoute(DELETE("/appointments/{publicId}"), appointmentHandler::delete)
		.andRoute(PUT("/appointments/{publicId}/reschedule"), appointmentHandler::reschedule)
		.andRoute(GET("/appointments/{publicId}/history"), appointmentHandler::history)
		
		// ServiceProvider
		.andRoute(GET("/appointments"), serviceProviderHandler::all)
		.andRoute(GET("/appointments/{publicId}"), serviceProviderHandler::byPublicId)
		.andRoute(POST("/appointments"), serviceProviderHandler::create)
		.andRoute(PUT("/appointments"), serviceProviderHandler::update)
		.andRoute(DELETE("/appointments/{publicId}"), serviceProviderHandler::delete);
    }
}
