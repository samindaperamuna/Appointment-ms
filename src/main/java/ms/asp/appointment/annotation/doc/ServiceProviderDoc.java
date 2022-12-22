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
import ms.asp.appointment.handler.ServiceProviderHandler;
import ms.asp.appointment.model.serviceprovider.Schedule;
import ms.asp.appointment.model.serviceprovider.ServiceProviderModel;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.TYPE })
@RouterOperations({
	@RouterOperation(
		path = "/serviceproviders",
		produces = { MediaType.APPLICATION_JSON_VALUE },
		method = RequestMethod.GET,
		beanClass = ServiceProviderHandler.class,
		beanMethod = "all",
		operation = @Operation(
			tags = "Service Provider",
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
			tags = "Service Provider",
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
			tags = "Service Provider",
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
			tags = "Service Provider",
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
			tags = "Service Provider",
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
			tags = "Service Provider",
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
			tags = "Service Provider",
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
public @interface ServiceProviderDoc {

}
