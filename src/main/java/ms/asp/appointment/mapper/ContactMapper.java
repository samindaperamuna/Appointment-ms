package ms.asp.appointment.mapper;

import static ms.asp.appointment.util.CommonUtils.generatePublicId;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ms.asp.appointment.domain.Contact;
import ms.asp.appointment.model.ContactModel;

@Mapper(config = BaseMapper.class)
public interface ContactMapper extends BaseMapper<Contact, ContactModel> {

    @InheritConfiguration
    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "mapPublicIdContact")
    Contact toEntity(ContactModel model);

    @Named("mapPublicIdContact")
    default String mapPublicId(String publicId) {
	if (publicId == null || publicId.isBlank()) {
	    return generatePublicId();
	}

	return publicId;
    }
}
