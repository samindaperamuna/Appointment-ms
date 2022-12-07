package ms.asp.appointment.mapper;

import static ms.asp.appointment.util.CommonUtils.generatePublicId;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import ms.asp.appointment.domain.Contact;
import ms.asp.appointment.model.ContactModel;

@Mapper
public interface ContactMapper {

    ContactModel map(Contact contact);

    @Mapping(target = "publicId", source = "publicId", qualifiedByName = "contactPublicId")
    Contact map(ContactModel model);

    @Named("contactPublicId")
    default String map(String publicId) {
	if (publicId == null || publicId.isBlank()) {
	    return generatePublicId();
	}

	return publicId;
    }
}
