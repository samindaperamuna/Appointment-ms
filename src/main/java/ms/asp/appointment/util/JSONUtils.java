package ms.asp.appointment.util;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import ms.asp.appointment.domain.ServiceType;

@Component
@Slf4j
public class JSONUtils {

    private final static ObjectMapper mapper = new ObjectMapper();

    public static String objectToJSON(Object object) {
	try {
	    return mapper.writeValueAsString(object);
	} catch (JsonMappingException e) {
	    log.error("Error mapping JSON: " + e.getLocalizedMessage());
	    e.printStackTrace();
	} catch (JsonProcessingException e) {
	    log.error("Error processing JSON: " + e.getLocalizedMessage());
	}

	return "";
    }

    public static Object jsonToObject(String json, TypeReference<?> typeRef) {
	Object object = new Object();

	try {
	    object = mapper.readValue(json, typeRef);
	} catch (JsonMappingException e) {
	    log.error("Error mapping JSON: " + e.getLocalizedMessage());
	    e.printStackTrace();
	} catch (JsonProcessingException e) {
	    log.error("Error processing JSON: " + e.getLocalizedMessage());
	}

	return object;
    }

    public static String serviceTypeToJSON(Set<ServiceType> serviceTypes) {
	Set<String> set = new HashSet<>();

	for (ServiceType enumeration : serviceTypes) {
	    set.add(enumeration.getValue());
	}

	return objectToJSON(set);
    }

    @SuppressWarnings("unchecked")
    public static Set<ServiceType> jsonToServiceType(String json) {
	return (Set<ServiceType>) jsonToObject(json, new TypeReference<Set<ServiceType>>() {});
    }
}
