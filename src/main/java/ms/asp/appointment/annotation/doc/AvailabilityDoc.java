package ms.asp.appointment.annotation.doc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import ms.asp.appointment.handler.AvailabilityHandler;
import ms.asp.appointment.model.availability.AvailabilityModel;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@RouterOperations({
	@RouterOperation(
		path = "/availability/{publicId}",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.GET,
		beanClass = AvailabilityHandler.class,
		beanMethod = "byPublicId",
		operation = @Operation(
			tags = "Availability",
			operationId = "getAvailabilityByPublicId",
			responses = {
				@ApiResponse(
					responseCode = "200",
					description = "Successful operation",
					content = @Content(
						schema = @Schema(implementation = AvailabilityModel.class))),
				@ApiResponse(responseCode = "404", description = "Availability not found"),
				@ApiResponse(responseCode = "400", description = "Couldn't fetch availability")
			},
			parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId") })),
	@RouterOperation(
		path = "/availability",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.POST,
		beanClass = AvailabilityHandler.class,
		beanMethod = "create",
		operation = @Operation(
			tags = "Availability",
			operationId = "createAvailability",
			responses = {
				@ApiResponse(
					responseCode = "200",
					description = "Successful operation",
					content = @Content(
						schema = @Schema(implementation = AvailabilityModel.class))),
				@ApiResponse(responseCode = "400", description = "Couldn't create slot")
			},
			requestBody = @RequestBody(
				content = @Content(
					schema = @Schema(implementation = AvailabilityModel.class))))),
	@RouterOperation(
		path = "/availability",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.PUT,
		beanClass = AvailabilityHandler.class,
		beanMethod = "update",
		operation = @Operation(
			tags = "Availability",
			operationId = "updateAvailability",
			responses = {
				@ApiResponse(
					responseCode = "200",
					description = "Successful operation",
					content = @Content(
						schema = @Schema(implementation = AvailabilityModel.class))),
				@ApiResponse(responseCode = "404", description = "Availability not found"),
				@ApiResponse(responseCode = "400", description = "Couldn't update availability")
			},
			requestBody = @RequestBody(
				content = @Content(
					schema = @Schema(implementation = AvailabilityModel.class))))),
	@RouterOperation(
		path = "/availability/{publicId}",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.DELETE,
		beanClass = AvailabilityHandler.class,
		beanMethod = "delete",
		operation = @Operation(
			tags = "Availability",
			operationId = "deleteAvailability",
			responses = {
				@ApiResponse(
					responseCode = "200",
					description = "Successful operation",
					content = @Content(
						schema = @Schema(implementation = AvailabilityModel.class))),
				@ApiResponse(responseCode = "404", description = "Availability not found"),
				@ApiResponse(responseCode = "400", description = "Couldn't delete availability")
			},
			parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId") })),
})
public @interface AvailabilityDoc {

}
