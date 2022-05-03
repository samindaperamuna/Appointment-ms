package ms.asp.appointment.controllers.advices;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import ms.asp.appointment.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class ResponseControllerAdvice implements ResponseBodyAdvice {

  @Override
  public boolean supports(MethodParameter returnType, Class converterType) {
    return returnType.getParameterType().equals(Optional.class);
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
      Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
    if (returnType.getParameterType().equals(Optional.class)) {
      return ((Optional<?>) body).orElseThrow(() -> new NotFoundException(
          "No resource found @ " + request.getURI().getPath()));
    }
    if(Objects.isNull(body)){
      throw new NotFoundException(
          "No resource found @ " + request.getURI().getPath());
    }
    return body;
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Map<String, String>> handle(NotFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

}
