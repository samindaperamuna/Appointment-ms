package ms.asp.appointment.service;

import java.io.Serializable;

import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;
import ms.asp.appointment.domain.BaseEntity;
import ms.asp.appointment.mapper.BaseMapper;
import ms.asp.appointment.model.BaseModel;
import ms.asp.appointment.repository.BaseRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AbstractService<T extends BaseEntity, ID extends Serializable, R extends BaseModel> {

    protected final BaseRepository<T, ID> repository;
    protected final BaseMapper<T, R> mapper;

    public Flux<R> findAll(Pageable pageable) {
	return repository.findBy(pageable)
		.map(mapper::toModel);
    }

    public Mono<R>findOne(String publicId) {
	return repository.findByPublicId(publicId)
		.map(mapper::toModel);
    }

    public Mono<R> delete(String publicId) {
	return repository.deleteByPublicId(publicId)
		.map(mapper::toModel);
    }
}
