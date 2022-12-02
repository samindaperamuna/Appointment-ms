package ms.asp.appointment.mapper;

import java.util.Collection;

import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;

import ms.asp.appointment.domain.BaseEntity;
import ms.asp.appointment.model.BaseModel;

@MapperConfig
public interface BaseMapper<E extends BaseEntity, M extends BaseModel> {

    M toModel(E e);

    @Mapping(target = "publicId", ignore = true)
    E toEntity(M m);

    Collection<M> toModel(Collection<E> e);
}
