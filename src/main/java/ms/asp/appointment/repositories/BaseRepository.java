package ms.asp.appointment.repositories;

import java.io.Serializable;
import java.util.Optional;
import ms.asp.appointment.domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.history.RevisionRepository;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID extends Serializable> extends JpaRepository<T , ID> ,
    PagingAndSortingRepository<T, ID> , RevisionRepository<T, ID, Long> {

  Optional<T> findByPublicId(String publicId);

  void deleteByPublicId(String publicId);
}
