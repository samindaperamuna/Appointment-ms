package ms.asp.appointment.exception;

import java.util.Map;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class ReactiveExceptionHandler extends AbstractErrorWebExceptionHandler {
    private final Map<Class<? extends Exception>, HttpStatus> exceptionToStatusCode;
    private final HttpStatus defaultStatus;

    public ReactiveExceptionHandler(ErrorAttributes errorAttributes,
	    WebProperties webProperties,
	    ApplicationContext applicationContext,
	    Map<Class<? extends Exception>, HttpStatus> exceptionToStatusCode,
	    HttpStatus defaultStatus,
	    ServerCodecConfigurer configurer) {

	super(errorAttributes, webProperties.getResources(), applicationContext);
	this.exceptionToStatusCode = exceptionToStatusCode;
	this.defaultStatus = defaultStatus;
	this.setMessageWriters(configurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
	return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
	Throwable error = getError(request);

	log.error("An error has occurred", error);

	HttpStatus httpStatus;

	if (error instanceof Exception) {
	    httpStatus = exceptionToStatusCode.getOrDefault(error.getClass(), defaultStatus);
	} else {
	    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	return ServerResponse.status(httpStatus)
		.contentType(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromValue(
			ErrorResponse.builder()
				.code(httpStatus.value())
				.message(error.getMessage())
				.build()));
    }
}