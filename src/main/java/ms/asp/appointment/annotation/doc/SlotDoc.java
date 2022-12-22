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
import ms.asp.appointment.handler.SlotHandler;
import ms.asp.appointment.model.slot.SlotModel;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@RouterOperations({
	@RouterOperation(
		path = "/slots/{publicId}",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.GET,
		beanClass = SlotHandler.class,
		beanMethod = "byPublicId",
		operation = @Operation(
			tags = "Slot",
			operationId = "getSlotByPublicId",
			responses = {
				@ApiResponse(
					responseCode = "200",
					description = "Successful operation",
					content = @Content(
						schema = @Schema(implementation = SlotModel.class))),
				@ApiResponse(responseCode = "404", description = "Slot not found"),
				@ApiResponse(responseCode = "400", description = "Couldn't fetch slot")
			},
			parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId") })),
	@RouterOperation(
		path = "/slots",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.POST,
		beanClass = SlotHandler.class,
		beanMethod = "create",
		operation = @Operation(
			tags = "Slot",
			operationId = "createSlot",
			responses = {
				@ApiResponse(
					responseCode = "200",
					description = "Successful operation",
					content = @Content(
						schema = @Schema(implementation = SlotModel.class))),
				@ApiResponse(responseCode = "400", description = "Couldn't create slot")
			},
			requestBody = @RequestBody(
				content = @Content(
					schema = @Schema(implementation = SlotModel.class))))),
	@RouterOperation(
		path = "/slots",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.PUT,
		beanClass = SlotHandler.class,
		beanMethod = "update",
		operation = @Operation(
			tags = "Slot",
			operationId = "updateSlot",
			responses = {
				@ApiResponse(
					responseCode = "200",
					description = "Successful operation",
					content = @Content(
						schema = @Schema(implementation = SlotModel.class))),
				@ApiResponse(responseCode = "404", description = "Slot not found"),
				@ApiResponse(responseCode = "400", description = "Couldn't update slot")
			},
			requestBody = @RequestBody(
				content = @Content(
					schema = @Schema(implementation = SlotModel.class))))),
	@RouterOperation(
		path = "/slots/{publicId}",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.DELETE,
		beanClass = SlotHandler.class,
		beanMethod = "delete",
		operation = @Operation(
			tags = "Slot",
			operationId = "deleteSlot",
			responses = {
				@ApiResponse(
					responseCode = "200",
					description = "Successful operation",
					content = @Content(
						schema = @Schema(implementation = SlotModel.class))),
				@ApiResponse(responseCode = "404", description = "Slot not found"),
				@ApiResponse(responseCode = "400", description = "Couldn't delete slot")
			},
			parameters = { @Parameter(in = ParameterIn.PATH, name = "publicId") })),
})
public @interface SlotDoc {

}
