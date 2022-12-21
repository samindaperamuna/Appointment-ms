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
import ms.asp.appointment.handler.AppointmentFlowHandler;
import ms.asp.appointment.model.AppointmentFlowModel;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@RouterOperations({
	@RouterOperation(
		path = "/appointmentflows",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.GET,
		beanClass = AppointmentFlowHandler.class,
		beanMethod = "all",
		operation = @Operation(
			tags = "Appointment Flow",
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
			tags = "Appointment Flow",
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
			tags = "Appointment Flow",
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
			tags = "Appointment Flow",
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
			tags = "Appointment Flow",
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
public @interface AppointmentFlowDoc {

}
