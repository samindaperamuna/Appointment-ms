package ms.asp.appointment.mappers;

import java.util.Objects;
import java.util.Set;
import ms.asp.appointment.domain.CodeableConcept;
import ms.asp.appointment.models.CodeableConceptModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CodeableMapper extends BaseMapper<CodeableConcept, CodeableConceptModel>{

  @Mapping(target = "display")
  Set<CodeableConcept> map(Set<String> display);


  @Mapping(target = "version", ignore = true)
  @Mapping(target = "system", ignore = true)
  @Mapping(target = "publicId", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "code", ignore = true)
  @Mapping(target = "display")
  CodeableConcept map(String display);

  String map(CodeableConcept display);

}
