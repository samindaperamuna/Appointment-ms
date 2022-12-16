package ms.asp.appointment.config;

import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.Bundle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;

@Configuration
public class FHIRClientConfig {

    @Value("${application.fhir.server.url}")
    private String serverBase;

    public Bundle getFHIRBundle() {
	FhirContext ctx = FhirContext.forR4();
	IGenericClient client = ctx.newRestfulGenericClient(serverBase);

	return client.search()
		.forResource(Appointment.class)
		.returnBundle(Bundle.class)
		.execute();
    }
}
