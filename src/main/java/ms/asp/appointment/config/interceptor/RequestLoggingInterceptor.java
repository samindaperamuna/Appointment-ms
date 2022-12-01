package ms.asp.appointment.config.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class RequestLoggingInterceptor extends ServerHttpRequestDecorator {

    private boolean logHeaders;

    public RequestLoggingInterceptor(ServerHttpRequest delegate, boolean logHeaders) {
	super(delegate);

	this.logHeaders = logHeaders;
    }

    @Override
    public Flux<DataBuffer> getBody() {

	return super.getBody().doOnNext(dataBuffer -> {

	    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
		Channels.newChannel(baos).write(dataBuffer.asByteBuffer().asReadOnlyBuffer());
		String body = IOUtils.toString(baos.toByteArray(), "UTF-8");

		if (logHeaders) {
		    log.info("Request: method={}, uri={}, headers={}, payload={}, audit={}",
			    getDelegate().getMethod(),
			    getDelegate().getPath(),
			    getDelegate().getHeaders(),
			    body,
			    true);
		} else {
		    log.info("Request: method={}, uri={}, payload={}, audit={}",
			    getDelegate().getMethod(),
			    getDelegate().getPath(),
			    body,
			    true);
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	});
    }
}
