package ms.asp.appointment.mapper;

import static ms.asp.appointment.util.CommonUtils.generatePublicId;

import java.util.Collection;

import org.mapstruct.MapperConfig;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ms.asp.appointment.domain.BaseEntity;
import ms.asp.appointment.model.BaseModel;

@MapperConfig
public interface BaseMapper<E extends BaseEntity, M extends BaseModel> {

    M toModel(E e);

    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "publicIdMapping")
    E toEntity(M m);

    Collection<M> toModel(Collection<E> e);

    @Named("publicIdMapping")
    default String map(String publicId) {
	if (publicId == null || publicId.isBlank()) {
	    return generatePublicId();
	}

	return publicId;
    }
}
