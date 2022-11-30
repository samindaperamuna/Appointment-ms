package ms.asp.appointment.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ExceptionConfig {

    @Bean
    public Map<Class<? extends Exception>, HttpStatus> exceptionToStatusCode() {
	Map<Class<? extends Exception>, HttpStatus> exceptionsMap = new HashMap<>();
	exceptionsMap.put(NullPointerException.class, HttpStatus.BAD_REQUEST);
	exceptionsMap.put(ArrayIndexOutOfBoundsException.class, HttpStatus.INTERNAL_SERVER_ERROR);

	return exceptionsMap;
    }

    @Bean
    public HttpStatus defaultStatus() {
	return HttpStatus.BAD_REQUEST;
    }
}
