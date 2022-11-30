package ms.asp.appointment.mapper;

import java.util.Collection;

import org.mapstruct.MapperConfig;

import ms.asp.appointment.domain.BaseEntity;
import ms.asp.appointment.model.BaseModel;

@MapperConfig
public interface BaseMapper<E extends BaseEntity, M extends BaseModel> {

    M toModel(E e);
    E toEntity(M m);
    Collection<M> toModel(Collection<E> e);
}
