package ms.asp.appointment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
		title = "Appointment Micro-service",
		description = "Documentation for available API endpoints.",
		version = "v1"))
public class Application {

    public static void main(String[] args) {
	SpringApplication.run(Application.class, args);
    }
}
