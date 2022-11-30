package ms.asp.appointment.config;

import org.hl7.fhir.dstu2.model.Appointment;
import org.hl7.fhir.dstu2.model.Bundle;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class FHIRClientConfig {

    private final Environment environment;

    public Bundle getFHIRBundle() {
	FhirContext ctx = FhirContext.forDstu2();
	String serverBase = environment.getProperty("fhir.server.url");

	IGenericClient client = ctx.newRestfulGenericClient(serverBase);

	return client.search()
		.forResource(Appointment.class)
		.returnBundle(Bundle.class)
		.execute();
    }
}
