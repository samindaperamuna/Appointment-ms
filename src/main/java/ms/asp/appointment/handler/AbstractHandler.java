package ms.asp.appointment.handler;

import java.io.Serializable;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import ms.asp.appointment.domain.BaseEntity;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.model.BaseModel;
import ms.asp.appointment.service.AbstractService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public abstract class AbstractHandler<E extends BaseEntity, ID extends Serializable, M extends BaseModel, S extends AbstractService<E, ID, M>> {

    protected static final String ID_TEMPLATE = "publicId";

    protected final S service;
    protected final M model;

    public Mono<ServerResponse> all(ServerRequest req) {
	var page = req.queryParam("page");
	var size = req.queryParam("size");

	if (page.isEmpty() || !NumberUtils.isCreatable(page.get())) {
	    return Mono.error(new NotFoundException("Query parameter 'page' required"));
	} else if (size.isEmpty() || !NumberUtils.isCreatable(size.get())) {
	    return Mono.error(new NotFoundException("Query parameter 'size' required"));
	}

	PageRequest pageReq = PageRequest.of(Integer.parseInt(page.get()), Integer.parseInt(size.get()));

	return ServerResponse.ok().body(service.findAll(pageReq), model.getClass())
		.onErrorResume(e -> Mono.error(e));
    }

    public Mono<ServerResponse> byPublicId(ServerRequest req) {
	var id = req.pathVariable(ID_TEMPLATE);

	return ServerResponse.ok().body(service.findOne(id),
		model.getClass())
		.onErrorResume(e -> {
		    return Mono.error(e);
		});
    }

    @SuppressWarnings("unchecked")
    public Mono<ServerResponse> create(ServerRequest req) {
	return req.bodyToMono(model.getClass())
		.flatMap(m -> ServerResponse.ok()
			.body(service.create((M) m), model.getClass()))
		.onErrorResume(e -> Mono.error(e));
    }

    @SuppressWarnings("unchecked")
    public Mono<ServerResponse> update(ServerRequest req) {
	return req.bodyToMono(model.getClass())
		.flatMap(m -> ServerResponse.ok().body(service.update((M) m), model.getClass()))
		.onErrorResume(e -> Mono.error(e));
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
	var id = req.pathVariable(ID_TEMPLATE);

	return ServerResponse.ok().body(service.delete(id), model.getClass())
		.onErrorResume(e -> Mono.error(e));
    }
}
