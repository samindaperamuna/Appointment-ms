package ms.asp.appointment.mapper;

import static ms.asp.appointment.util.CommonUtils.generatePublicId;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ms.asp.appointment.domain.Note;
import ms.asp.appointment.model.NoteModel;

@Mapper(config = BaseMapper.class)
public interface NoteMapper extends BaseMapper<Note, NoteModel> {

    @InheritConfiguration
    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "mapPublicIdNote")
    Note toEntity(NoteModel model);

    @Named("mapPublicIdNote")
    default String mapPublicId(String publicId) {
	if (publicId == null || publicId.isBlank()) {
	    return generatePublicId();
	}

	return publicId;
    }
}
