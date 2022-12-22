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
import ms.asp.appointment.model.participant.ParticipantInfoModel;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@RouterOperations({
	@RouterOperation(
		path = "/participantinfo/{publicId}",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.GET,
		operation = @Operation(
			tags = "Participant Info",
			operationId = "getParticipantInfoByPublicId",
			responses = {
				@ApiResponse(
					responseCode = "200",
					description = "Successful operation",
					content = @Content(
						schema = @Schema(implementation = ParticipantInfoModel.class))),
				@ApiResponse(responseCode = "404", description = "Participant info not found")
			},
			parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId") })),
	@RouterOperation(
		path = "/participantinfo",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.POST,
		operation = @Operation(
			tags = "Participant Info",
			operationId = "createParticipantInfo",
			responses = {
				@ApiResponse(
					responseCode = "200",
					description = "Successful operation",
					content = @Content(
						schema = @Schema(implementation = ParticipantInfoModel.class)))
			},
			requestBody = @RequestBody(
				content = @Content(
					schema = @Schema(implementation = ParticipantInfoModel.class))))),
	@RouterOperation(
		path = "/participantinfo",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.PUT,
		operation = @Operation(
			tags = "Participant Info",
			operationId = "updateParticipantInfo",
			responses = {
				@ApiResponse(
					responseCode = "200",
					description = "Successful operation",
					content = @Content(
						schema = @Schema(implementation = ParticipantInfoModel.class))),
				@ApiResponse(responseCode = "404", description = "Participant info not found")
			},
			requestBody = @RequestBody(
				content = @Content(
					schema = @Schema(implementation = ParticipantInfoModel.class))))),
	@RouterOperation(
		path = "/participantinfo/{publicId}",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.DELETE,
		operation = @Operation(
			tags = "Participant Info",
			operationId = "deleteParticipantInfo",
			responses = {
				@ApiResponse(
					responseCode = "200",
					description = "Successful operation",
					content = @Content(
						schema = @Schema(implementation = ParticipantInfoModel.class))),
				@ApiResponse(responseCode = "404", description = "Participant info not found")
			},
			parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId") })),
})
public @interface ParticipantInfoDoc {

}
