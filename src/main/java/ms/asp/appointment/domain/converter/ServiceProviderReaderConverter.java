package ms.asp.appointment.domain.converter;

import java.time.DayOfWeek;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ms.asp.appointment.domain.ServiceProvider;

@Slf4j
@RequiredArgsConstructor
public class ServiceProviderReaderConverter implements Converter<Row, ServiceProvider> {

    private final ObjectMapper mapper;

    @Override
    public ServiceProvider convert(Row row) {
	ServiceProvider serviceProvider = new ServiceProvider();	

	try {
	    var id = row.get("ID", Long.class);
	    var publicId = row.get("PUBLIC_ID", String.class);
	    var offDaysString = row.get("OFF_DAYS", String.class);
	    mapper.readValue(offDaysString, new TypeReference<Set<DayOfWeek>>() {
	    });

	    serviceProvider.setId(id);
	    serviceProvider.setPublicId(publicId);

	    return serviceProvider;
	} catch (JsonMappingException e) {
	    log.error("Error mapping JSON: " + e.getLocalizedMessage());
	    e.printStackTrace();
	} catch (JsonProcessingException e) {
	    log.error("Error processing JSON: " + e.getLocalizedMessage());
	}

	return serviceProvider;
    }
}
