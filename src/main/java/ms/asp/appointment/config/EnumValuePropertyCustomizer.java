package ms.asp.appointment.config;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springdoc.core.customizers.PropertyCustomizer;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;

@Component
public class EnumValuePropertyCustomizer implements PropertyCustomizer {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Schema customize(Schema property, AnnotatedType type) {
	if (property instanceof StringSchema && isEnumType(type)) {
	    ObjectMapper objectMapper = Json.mapper();

	    property.setEnum(Arrays.stream(((JavaType) type.getType()).getRawClass().getEnumConstants())
		    .map(e -> objectMapper.convertValue(e, String.class))
		    .collect(Collectors.toList()));
	}
	return property;
    }

    private boolean isEnumType(AnnotatedType type) {
	return type.getType() instanceof JavaType && ((JavaType) type.getType()).isEnumType();
    }
}
