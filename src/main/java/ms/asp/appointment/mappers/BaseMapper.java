package ms.asp.appointment.mappers;


import java.util.Collection;
import ms.asp.appointment.domain.BaseEntity;
import ms.asp.appointment.models.BaseModel;
import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;

@MapperConfig
public interface BaseMapper<E extends BaseEntity, M extends BaseModel>  {

  M toModel(E e);
  @Mapping(target = "version", ignore = true)
  @Mapping(target = "id", ignore = true)
  E toEntity(M m);

  Collection<M> toModel(Collection<E> e);

}
