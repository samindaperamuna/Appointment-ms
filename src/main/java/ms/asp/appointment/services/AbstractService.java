package ms.asp.appointment.services;

import java.io.Serializable;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import ms.asp.appointment.domain.BaseEntity;
import ms.asp.appointment.mappers.BaseMapper;
import ms.asp.appointment.models.BaseModel;
import ms.asp.appointment.repositories.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AbstractService<T extends BaseEntity, ID extends Serializable, RES extends BaseModel> {

  protected final BaseRepository<T, ID> repository;
  protected final BaseMapper<T, RES> mapper;

  public Flux<RES> findAll(Pageable pageable) {
    Page<T> page = repository.findAll(pageable);
    return Flux.fromIterable(mapper.toModel(page.toList()));
  }

  public Optional<T> findOne(String publicId){
    return repository.findByPublicId(publicId);
  }

  public Mono<RES> delete(String publicId) {
    repository.deleteByPublicId(publicId);
    return Mono.empty();
  }
}
