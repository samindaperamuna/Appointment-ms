package ms.asp.appointment.repository;

import java.io.Serializable;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

import ms.asp.appointment.domain.BaseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID extends Serializable> extends
	ReactiveCrudRepository<T, ID>, ReactiveSortingRepository<T, ID> {

    Flux<T> findBy(Pageable pageable);

    Mono<T> findByPublicId(String publicId);

    Mono<T> deleteByPublicId(String publicId);
}
