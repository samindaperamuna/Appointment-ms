package ms.asp.appointment.domain.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.r2dbc.core.Parameter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ms.asp.appointment.domain.ServiceProvider;

@Slf4j
@RequiredArgsConstructor
public class ServiceProviderWriterConverter implements Converter<ServiceProvider, OutboundRow> {

    private final ObjectMapper mapper;

    @Override
    public OutboundRow convert(ServiceProvider serviceProvider) {
	OutboundRow row = new OutboundRow();

	try {
	    row.put("ID", Parameter.fromOrEmpty(serviceProvider.getId(), Long.class));
	    row.put("PUBLIC_ID", Parameter.fromOrEmpty(serviceProvider.getPublicId(), String.class));
	    row.put("OFF_DAYS", Parameter.from(mapper.writeValueAsString(serviceProvider.getOffDays())));
	    
	    return row;
	} catch (JsonMappingException e) {
	    log.error("Error mapping JSON: " + e.getLocalizedMessage());
	    e.printStackTrace();
	} catch (JsonProcessingException e) {
	    log.error("Error processing JSON: " + e.getLocalizedMessage());
	}

	return row;
    }
}
