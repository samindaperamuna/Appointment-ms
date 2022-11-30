package ms.asp.appointment.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import ms.asp.appointment.domain.converter.ServiceCategoryReader;
import ms.asp.appointment.domain.converter.ServiceCategoryWriter;

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
		new ServiceCategoryReader(),
		new ServiceCategoryWriter());
    }
}
