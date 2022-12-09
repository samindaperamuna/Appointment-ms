package ms.asp.appointment.util;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;
import ms.asp.appointment.domain.ServiceType;

@Component
@Slf4j
public class JSONUtils {

    private final static ObjectMapper mapper = new ObjectMapper();

    static {
	mapper.registerModule(new JavaTimeModule());
    }

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

    public static String serviceTypeToJSON(List<ServiceType> serviceTypes) {
	List<String> list = new ArrayList<>();

	for (ServiceType enumeration : serviceTypes) {
	    list.add(enumeration.getValue());
	}

	return objectToJSON(list);
    }

    @SuppressWarnings("unchecked")
    public static List<ServiceType> jsonToServiceType(String json) {
	return (List<ServiceType>) jsonToObject(json, new TypeReference<List<ServiceType>>() {
	});
    }

    @SuppressWarnings("unchecked")
    public static List<DayOfWeek> jsonToOffDays(String json) {
	return (List<DayOfWeek>) jsonToObject(json, new TypeReference<List<DayOfWeek>>() {
	});
    }
}
