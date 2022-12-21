package ms.asp.appointment.annotation.doc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import ms.asp.appointment.handler.AppointmentHandler;
import ms.asp.appointment.model.AppointmentModel;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@RouterOperations({
	@RouterOperation(
		path = "/appointments",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.GET,
		beanClass = AppointmentHandler.class,
		beanMethod = "all",
		operation = @Operation(
			tags = "Appointment",
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
			tags = "Appointment",
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
			tags = "Appointment",
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
			tags = "Appointment",
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
			tags = "Appointment",
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
			tags = "Appointment",
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
			tags = "Appointment",
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
			tags = "Appointment",
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
public @interface AppointmentDoc {

}
