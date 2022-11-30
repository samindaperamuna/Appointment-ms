package ms.asp.appointment.integration;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
public class AppointmentIntegrationTest {

    @Autowired
    private WebTestClient webClient;
    
    @Test
    @Order(1)
    void saveAppointmentTest() {
	webClient.post()
		.uri("/appointments")
		.contentType(MediaType.APPLICATION_JSON)
		.body(BodyInserters.fromValue(""))
		.exchange()
		.expectStatus().isOk()
		.expectBody().json("");
    }
}
