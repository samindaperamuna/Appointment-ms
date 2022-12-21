package ms.asp.appointment.service;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import lombok.RequiredArgsConstructor;
import ms.asp.appointment.domain.BaseEntity;
import ms.asp.appointment.mapper.BaseMapper;
import ms.asp.appointment.model.BaseModel;
import ms.asp.appointment.repository.BaseRepository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public abstract class AbstractService<E extends BaseEntity, ID extends Serializable, M extends BaseModel> {

    protected final BaseRepository<E, ID> repository;
    protected final BaseMapper<E, M> mapper;

    public Mono<Page<M>> findAll(PageRequest pageRequest) {
	return this.repository.findBy(pageRequest)
		.flatMap(e -> findByPublicIdEager(e.getPublicId()))
		.map(e -> mapper.toModel(e))
		.collectList()
		.zipWith(this.repository.count())
		.map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    public Mono<M> findOne(String publicId) {
	return findByPublicIdEager(publicId)
		.map(mapper::toModel);
    }

    public Mono<M> create(M model) {
	return Mono.just(model)
		.map(mapper::toEntity)
		.flatMap(this::create)
		.map(mapper::toModel);
    }

    protected abstract Mono<E> create(E entitiy);

    public Mono<M> update(M model) {
	return Mono.just(model)
		.map(mapper::toEntity)
		.flatMap(this::update)
		.map(mapper::toModel);
    }

    protected abstract Mono<E> update(E entity);

    @SuppressWarnings("unchecked")
    public Mono<M> delete(String publicId) {
	return findByPublicIdEager(publicId)
		.flatMap(e -> delete((ID) e.getId()))
		.map(mapper::toModel);
    }

    protected abstract Mono<E> delete(ID id);

    public Mono<E> findByIdEager(ID id) {
	return repository.findById(id)
		.flatMap(e -> findByPublicIdEager(e.getPublicId()));
    }

    protected abstract Mono<E> findByPublicIdEager(String publicId);
}
