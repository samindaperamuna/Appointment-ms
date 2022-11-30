package ms.asp.appointment.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ms.asp.appointment.domain.CodeableConcept;
import ms.asp.appointment.model.CodeableConceptModel;

@Mapper(componentModel = "spring")
public interface CodeableMapper extends BaseMapper<CodeableConcept, CodeableConceptModel> {

    @Mapping(target = "display")
    Set<CodeableConcept> map(Set<String> display);

    default CodeableConcept map(String display) {
	var codeableConcept = new CodeableConcept();
	codeableConcept.setDisplay(display);

	return codeableConcept;
    }

    String map(CodeableConcept display);
}
