package ms.asp.appointment.config;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.media.Schema;
import ms.asp.appointment.util.CommonUtils;

@Configuration
public class SwaggerConfig {

    static {
	var timeSchema = new Schema<LocalTime>();
	var dateTimeSchema = new Schema<LocalDateTime>();
	timeSchema.example(LocalTime.now().format(DateTimeFormatter.ofPattern(CommonUtils.TIME_FORMAT)));
	dateTimeSchema.example(LocalDateTime.now().format(DateTimeFormatter.ofPattern(CommonUtils.DATE_TIME_FORMAT)));

	SpringDocUtils.getConfig()
		.replaceWithSchema(LocalTime.class, timeSchema)
		.replaceWithSchema(LocalDateTime.class, dateTimeSchema);
    }
}
