package ms.asp.appointment.handler;

import java.io.Serializable;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import ms.asp.appointment.domain.BaseEntity;
import ms.asp.appointment.model.BaseModel;
import ms.asp.appointment.service.AbstractService;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public abstract class AbstractHandler<E extends BaseEntity, ID extends Serializable, M extends BaseModel, S extends AbstractService<E, ID, M>> {

    private static final String idTemplate = "publicId";

    private final S service;
    private final M model;

    public Mono<ServerResponse> byPublicId(ServerRequest req) {
	var id = req.pathVariable(idTemplate);

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
	var id = req.pathVariable(idTemplate);

	return ServerResponse.ok().body(service.delete(id), model.getClass())
		.onErrorResume(e -> Mono.error(e));
    }
}
