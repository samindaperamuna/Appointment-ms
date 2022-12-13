package ms.asp.appointment.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import ms.asp.appointment.exception.AppointmentException;
import ms.asp.appointment.exception.AppointmentFlowException;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.exception.ServiceProviderException;

@Configuration
public class ExceptionConfig {

    @Bean
    public Map<Class<? extends Exception>, HttpStatus> exceptionToStatusCode() {
	Map<Class<? extends Exception>, HttpStatus> exceptionsMap = new HashMap<>();
	exceptionsMap.put(NotFoundException.class, HttpStatus.NOT_FOUND);
	exceptionsMap.put(AppointmentException.class, HttpStatus.BAD_REQUEST);
	exceptionsMap.put(ServiceProviderException.class, HttpStatus.BAD_REQUEST);
	exceptionsMap.put(AppointmentFlowException.class, HttpStatus.BAD_REQUEST);
	exceptionsMap.put(NullPointerException.class, HttpStatus.INTERNAL_SERVER_ERROR);
	exceptionsMap.put(ArrayIndexOutOfBoundsException.class, HttpStatus.INTERNAL_SERVER_ERROR);

	return exceptionsMap;
    }

    @Bean
    public HttpStatus defaultStatus() {
	return HttpStatus.BAD_REQUEST;
    }
}
