package ms.asp.appointment.configs;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.dstu2.model.Appointment;
import org.hl7.fhir.dstu2.model.Bundle;
import org.springframework.core.env.Environment;

// non spring config need to be refactored in near future
public class FhirClientConfig {

  private FhirClientConfig(){}

  private static Environment environment;


  public static void environment(Environment env){
    environment = env;
    FhirContext ctx = FhirContext.forDstu2();
    String serverBase = env.getProperty("fhir.server.url");

    IGenericClient client = ctx.newRestfulGenericClient(serverBase);

 // Perform a search
    Bundle results = client
        .search()
        .forResource(Appointment.class)
        .returnBundle(Bundle.class)
        .execute();
  }
}
