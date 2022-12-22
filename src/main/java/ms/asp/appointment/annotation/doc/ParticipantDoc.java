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
import ms.asp.appointment.model.appointment.ParticipantModel;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@RouterOperations({
	@RouterOperation(
		path = "/participants/{publicId}",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.GET,
		operation = @Operation(
			tags = "Participant",
			operationId = "getParticipantByPublicId",
			responses = {
				@ApiResponse(
					responseCode = "200",
					description = "Successful operation",
					content = @Content(
						schema = @Schema(implementation = ParticipantModel.class))),
				@ApiResponse(responseCode = "404", description = "Participant not found")
			},
			parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId") })),
	@RouterOperation(
		path = "/participants",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.POST,
		operation = @Operation(
			tags = "Participant",
			operationId = "createParticipant",
			responses = {
				@ApiResponse(
					responseCode = "200",
					description = "Successful operation",
					content = @Content(
						schema = @Schema(implementation = ParticipantModel.class)))
			},
			requestBody = @RequestBody(
				content = @Content(
					schema = @Schema(implementation = ParticipantModel.class))))),
	@RouterOperation(
		path = "/participants",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.PUT,
		operation = @Operation(
			tags = "Participant",
			operationId = "updateParticipant",
			responses = {
				@ApiResponse(
					responseCode = "200",
					description = "Successful operation",
					content = @Content(
						schema = @Schema(implementation = ParticipantModel.class))),
				@ApiResponse(responseCode = "404", description = "Participant not found")
			},
			requestBody = @RequestBody(
				content = @Content(
					schema = @Schema(implementation = ParticipantModel.class))))),
	@RouterOperation(
		path = "/participants/{publicId}",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.DELETE,
		operation = @Operation(
			tags = "Participant",
			operationId = "deleteParticipant",
			responses = {
				@ApiResponse(
					responseCode = "200",
					description = "Successful operation",
					content = @Content(
						schema = @Schema(implementation = ParticipantModel.class))),
				@ApiResponse(responseCode = "404", description = "Participant not found")
			},
			parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId") })),
})
public @interface ParticipantDoc {

}
