package ms.asp.appointment.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import ms.asp.appointment.domain.converter.AppointmentTypeReader;
import ms.asp.appointment.domain.converter.AppointmentTypeWriter;
import ms.asp.appointment.domain.converter.AvailabilityTypeReader;
import ms.asp.appointment.domain.converter.AvailabilityTypeWriter;
import ms.asp.appointment.domain.converter.CancellationReasonReader;
import ms.asp.appointment.domain.converter.CancellationReasonWriter;
import ms.asp.appointment.domain.converter.ServiceCategoryReader;
import ms.asp.appointment.domain.converter.ServiceCategoryWriter;
import ms.asp.appointment.domain.converter.ServiceTypeReader;
import ms.asp.appointment.domain.converter.ServiceTypeWriter;
import ms.asp.appointment.domain.converter.SpecialityReader;
import ms.asp.appointment.domain.converter.SpecialityWriter;

@Configuration
@RequiredArgsConstructor
public class R2DBCConfig extends AbstractR2dbcConfiguration {

    @Value("${spring.r2dbc.url}")
    private String r2dbcUrl;

    @Override
    public ConnectionFactory connectionFactory() {
	return ConnectionFactories.get(r2dbcUrl);
    }

    @Override
    protected List<Object> getCustomConverters() {
	return List.of(
		new AppointmentTypeReader(),
		new AppointmentTypeWriter(),
		new CancellationReasonReader(),
		new CancellationReasonWriter(),
		new ServiceCategoryReader(),
		new ServiceCategoryWriter(),
		new SpecialityReader(),
		new SpecialityWriter(),
		new AvailabilityTypeReader(),
		new AvailabilityTypeWriter(),
		new ServiceTypeReader(),
		new ServiceTypeWriter());
    }
}
