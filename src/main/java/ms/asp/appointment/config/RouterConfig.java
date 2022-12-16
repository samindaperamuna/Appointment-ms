package ms.asp.appointment.config;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import ms.asp.appointment.handler.AppointmentFlowHandler;
import ms.asp.appointment.handler.AppointmentHandler;
import ms.asp.appointment.handler.ServiceProviderHandler;
import ms.asp.appointment.model.AppointmentFlowModel;
import ms.asp.appointment.model.AppointmentModel;
import ms.asp.appointment.model.Schedule;
import ms.asp.appointment.model.ServiceProviderModel;

@Configuration
@RequiredArgsConstructor
public class RouterConfig {

    private final AppointmentHandler appointmentHandler;
    private final ServiceProviderHandler serviceProviderHandler;
    private final AppointmentFlowHandler appointmentFlowHandler;

    @Bean
    @RouterOperations({
	    @RouterOperation(
		    path = "/appointments",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.GET,
		    beanClass = AppointmentHandler.class,
		    beanMethod = "all",
		    operation = @Operation(
			    operationId = "getAllAppointments",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(schema = @Schema(implementation = Pageable.class))),
				    @ApiResponse(responseCode = "400", description = "Couldn't fetch appointments")
			    },
			    parameters = {
				    @Parameter(in = ParameterIn.QUERY, name = "page"),
				    @Parameter(in = ParameterIn.QUERY, name = "size")
			    })),
	    @RouterOperation(
		    path = "/appointments/{publicId}",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.GET,
		    beanClass = AppointmentHandler.class,
		    beanMethod = "byPublicId",
		    operation = @Operation(
			    operationId = "getAppointmentByPublicId",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(
						    schema = @Schema(implementation = AppointmentModel.class))),
				    @ApiResponse(responseCode = "404", description = "Appointment not found"),
				    @ApiResponse(responseCode = "400", description = "Couldn't fetch appointment")
			    },
			    parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId") })),
	    @RouterOperation(
		    path = "/appointments",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.POST,
		    beanClass = AppointmentHandler.class,
		    beanMethod = "create",
		    operation = @Operation(
			    operationId = "createAppointment",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(
						    schema = @Schema(implementation = AppointmentModel.class))),
				    @ApiResponse(responseCode = "404", description = "Service provider not found"),
				    @ApiResponse(responseCode = "400", description = "Service provider not defined"),
				    @ApiResponse(responseCode = "400", description = "Period not defined"),
				    @ApiResponse(responseCode = "400", description = "No participants defined"),
				    @ApiResponse(responseCode = "400", description = "Couldn't create appointment")
			    },
			    requestBody = @RequestBody(
				    content = @Content(schema = @Schema(implementation = AppointmentModel.class))))),
	    @RouterOperation(
		    path = "/appointments",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.PUT,
		    beanClass = AppointmentHandler.class,
		    beanMethod = "update",
		    operation = @Operation(
			    operationId = "updateAppointment",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(
						    schema = @Schema(implementation = AppointmentModel.class))),
				    @ApiResponse(responseCode = "404", description = "Appointment not found"),
				    @ApiResponse(responseCode = "404", description = "Service provider not found"),
				    @ApiResponse(responseCode = "400", description = "Service provider not defined"),
				    @ApiResponse(responseCode = "400", description = "Period not found"),
				    @ApiResponse(responseCode = "400", description = "Period not defined"),
				    @ApiResponse(responseCode = "400", description = "No participants defined"),
				    @ApiResponse(responseCode = "400", description = "Couldn't update appointment")
			    },
			    requestBody = @RequestBody(
				    content = @Content(schema = @Schema(implementation = AppointmentModel.class))))),
	    @RouterOperation(
		    path = "/appointments/{publicId}",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.DELETE,
		    beanClass = AppointmentHandler.class,
		    beanMethod = "delete",
		    operation = @Operation(
			    operationId = "deleteAppointment",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(
						    schema = @Schema(implementation = AppointmentModel.class))),
				    @ApiResponse(responseCode = "404", description = "Appointment not found"),
				    @ApiResponse(responseCode = "400", description = "Couldn't delete appointment")
			    },
			    parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId") })),
	    @RouterOperation(
		    path = "/appointments/{publicId}/reschedule",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.PUT,
		    beanClass = AppointmentHandler.class,
		    beanMethod = "reschedule",
		    operation = @Operation(
			    operationId = "rescheduleAppointment",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(
						    schema = @Schema(implementation = AppointmentModel.class))),
				    @ApiResponse(responseCode = "404", description = "Appointment not found"),
				    @ApiResponse(responseCode = "400", description = "Couldn't reschedule appointment")
			    },
			    parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId") })),
	    @RouterOperation(
		    path = "/appointments/{publicId}/history",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.GET,
		    beanClass = AppointmentHandler.class,
		    beanMethod = "history",
		    operation = @Operation(
			    operationId = "getAppointmentHistory",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(
						    schema = @Schema(implementation = AppointmentModel.class))),
				    @ApiResponse(responseCode = "404", description = "Appointment not found"),
				    @ApiResponse(responseCode = "400", description = "Couldn't fetch history")
			    },
			    parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId") })),
	    @RouterOperation(
		    path = "/appointments/{publicId}/calendar",
		    produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE },
		    method = RequestMethod.GET,
		    beanClass = AppointmentHandler.class,
		    beanMethod = "calendar",
		    operation = @Operation(
			    operationId = "getAppointmentCalendarFile",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(
						    schema = @Schema(implementation = AppointmentModel.class))),
				    @ApiResponse(responseCode = "404", description = "Appointment not found"),
				    @ApiResponse(responseCode = "400", description = "Couldn't create calendar file")
			    },
			    parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId") })),

    })
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
		.andRoute(GET("/appointments/{publicId}/fhir"),  appointmentHandler::fhir);
    }

    @Bean
    @RouterOperations({
	    @RouterOperation(
		    path = "/serviceproviders",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.GET,
		    beanClass = ServiceProviderHandler.class,
		    beanMethod = "all",
		    operation = @Operation(
			    operationId = "getAllServiceProviders",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(schema = @Schema(implementation = Pageable.class))),
				    @ApiResponse(responseCode = "400", description = "Couldn't fetch service providers")
			    },
			    parameters = {
				    @Parameter(in = ParameterIn.QUERY, name = "page"),
				    @Parameter(in = ParameterIn.QUERY, name = "size")
			    })),
	    @RouterOperation(
		    path = "/serviceproviders/{publicId}",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.GET,
		    beanClass = ServiceProviderHandler.class,
		    beanMethod = "byPublicId",
		    operation = @Operation(
			    operationId = "getServiceProviderByPublicId",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(
						    schema = @Schema(implementation = ServiceProviderModel.class))),
				    @ApiResponse(responseCode = "404", description = "Service provider not found"),
				    @ApiResponse(responseCode = "400", description = "Couldn't fetch service provider")
			    },
			    parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId") })),
	    @RouterOperation(
		    path = "/serviceproviders",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.POST,
		    beanClass = ServiceProviderHandler.class,
		    beanMethod = "create",
		    operation = @Operation(
			    operationId = "createServiceProvider",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(
						    schema = @Schema(implementation = ServiceProviderModel.class))),
				    @ApiResponse(responseCode = "400", description = "Missing slot information"),
				    @ApiResponse(responseCode = "400", description = "Missing contact details"),
				    @ApiResponse(responseCode = "400", description = "Off days not provided"),
				    @ApiResponse(responseCode = "400", description = "Service types not defined"),
				    @ApiResponse(responseCode = "400", description = "Missing availability details"),
				    @ApiResponse(responseCode = "400", description = "Couldn't create service provider")
			    },
			    requestBody = @RequestBody(
				    content = @Content(
					    schema = @Schema(implementation = ServiceProviderModel.class))))),
	    @RouterOperation(
		    path = "/serviceproviders",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.PUT,
		    beanClass = ServiceProviderHandler.class,
		    beanMethod = "update",
		    operation = @Operation(
			    operationId = "updateServiceProvider",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(
						    schema = @Schema(implementation = ServiceProviderModel.class))),
				    @ApiResponse(responseCode = "404", description = "Service provider not found"),
				    @ApiResponse(responseCode = "404", description = "Contact not found"),
				    @ApiResponse(responseCode = "404", description = "Slot not found"),
				    @ApiResponse(responseCode = "404", description = "Availability info not found"),
				    @ApiResponse(responseCode = "400", description = "Couldn't update service provider")
			    },
			    requestBody = @RequestBody(
				    content = @Content(
					    schema = @Schema(implementation = ServiceProviderModel.class))))),
	    @RouterOperation(
		    path = "/serviceproviders/{publicId}",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.DELETE,
		    beanClass = ServiceProviderHandler.class,
		    beanMethod = "delete",
		    operation = @Operation(
			    operationId = "deleteServiceProvider",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(
						    schema = @Schema(implementation = ServiceProviderModel.class))),
				    @ApiResponse(responseCode = "404", description = "Service provider not found"),
				    @ApiResponse(responseCode = "400", description = "Couldn't delete service provider")
			    },
			    parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId") })),
	    @RouterOperation(
		    path = "/serviceproviders/{publicId}/schedule",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.GET,
		    beanClass = ServiceProviderHandler.class,
		    beanMethod = "getSchedule",
		    operation = @Operation(
			    operationId = "getServiceProviderSchedule",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(schema = @Schema(implementation = Schedule.class))),
				    @ApiResponse(responseCode = "404", description = "Service provider not found"),
				    @ApiResponse(responseCode = "400", description = "Couldn't fetch schedule")
			    },
			    parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId"),
				    @Parameter(in = ParameterIn.QUERY, name = "begin"),
				    @Parameter(in = ParameterIn.QUERY, name = "end")
			    })),
	    @RouterOperation(
		    path = "/serviceproviders/availability/{type}",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.GET,
		    beanClass = ServiceProviderHandler.class,
		    beanMethod = "getByAvailability",
		    operation = @Operation(
			    operationId = "getServiceProviderByAvailability",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(
						    schema = @Schema(implementation = ServiceProviderModel.class))),
				    @ApiResponse(responseCode = "404", description = "Availability type not found"),
				    @ApiResponse(responseCode = "400", description = "Couldn't fetch service providers")
			    },
			    parameters = { @Parameter(in = ParameterIn.PATH, name = "type") })),
    })
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
    @RouterOperations({
	    @RouterOperation(
		    path = "/appointmentflows",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.GET,
		    beanClass = AppointmentFlowHandler.class,
		    beanMethod = "all",
		    operation = @Operation(
			    operationId = "getAllAppointmentFlows",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(schema = @Schema(implementation = Pageable.class))),
				    @ApiResponse(responseCode = "400", description = "Couldn't fetch appointment flows")
			    },
			    parameters = {
				    @Parameter(in = ParameterIn.QUERY, name = "page"),
				    @Parameter(in = ParameterIn.QUERY, name = "size")
			    })),
	    @RouterOperation(
		    path = "/appointmentflows/{publicId}",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.GET,
		    beanClass = AppointmentFlowHandler.class,
		    beanMethod = "byPublicId",
		    operation = @Operation(
			    operationId = "getAppointmentFlowByPublicId",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(
						    schema = @Schema(implementation = AppointmentFlowModel.class))),
				    @ApiResponse(responseCode = "404", description = "Appointment flow not found"),
				    @ApiResponse(responseCode = "400", description = "Couldn't fetch appointment flow")
			    },
			    parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId") })),
	    @RouterOperation(
		    path = "/appointmentflows",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.POST,
		    beanClass = AppointmentFlowHandler.class,
		    beanMethod = "create",
		    operation = @Operation(
			    operationId = "createAppointmentFlow",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(
						    schema = @Schema(implementation = AppointmentFlowModel.class))),
				    @ApiResponse(responseCode = "400", description = "Service types not provided"),
				    @ApiResponse(responseCode = "400", description = "Couldn't create appointment flow")
			    },
			    requestBody = @RequestBody(
				    content = @Content(
					    schema = @Schema(implementation = AppointmentFlowModel.class))))),
	    @RouterOperation(
		    path = "/appointmentflows",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.PUT,
		    beanClass = AppointmentFlowHandler.class,
		    beanMethod = "update",
		    operation = @Operation(
			    operationId = "updateAppointmentFlow",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(
						    schema = @Schema(implementation = AppointmentFlowModel.class))),
				    @ApiResponse(responseCode = "404", description = "Appointment flow not found"),
				    @ApiResponse(responseCode = "400", description = "Couldn't update appointment flow")
			    },
			    requestBody = @RequestBody(
				    content = @Content(
					    schema = @Schema(implementation = AppointmentFlowModel.class))))),
	    @RouterOperation(
		    path = "/appointmentflows/{publicId}",
		    produces = { MediaType.APPLICATION_JSON_VALUE },
		    method = RequestMethod.DELETE,
		    beanClass = AppointmentFlowHandler.class,
		    beanMethod = "delete",
		    operation = @Operation(
			    operationId = "deleteAppointmentFlow",
			    responses = {
				    @ApiResponse(
					    responseCode = "200",
					    description = "Successful operation",
					    content = @Content(
						    schema = @Schema(implementation = AppointmentFlowModel.class))),
				    @ApiResponse(responseCode = "404", description = "Appointment flow not found"),
				    @ApiResponse(responseCode = "400", description = "Couldn't delete appointment flow")
			    },
			    parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId") })),
    })
    public RouterFunction<ServerResponse> appointmentFlowRoutes() {
	return RouterFunctions
		.route(GET("/appointmentflows"), appointmentFlowHandler::all)
		.andRoute(GET("/appointmentflows/{publicId}"), appointmentFlowHandler::byPublicId)
		.andRoute(POST("/appointmentflows"), appointmentFlowHandler::create)
		.andRoute(PUT("/appointmentflows"), appointmentFlowHandler::update)
		.andRoute(DELETE("/appointmentflows/{publicId}"), appointmentFlowHandler::delete);
    }
}
