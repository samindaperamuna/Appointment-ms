package ms.asp.appointment.mapper.fhir;

import java.util.List;

import org.hl7.fhir.r4.model.Appointment.AppointmentParticipantComponent;
import org.hl7.fhir.r4.model.Appointment.ParticipantRequired;
import org.hl7.fhir.r4.model.Appointment.ParticipationStatus;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Period;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ms.asp.appointment.model.ParticipantModel;
import ms.asp.appointment.model.PeriodModel;
import ms.asp.appointment.model.ServiceProviderModel;

@Mapper
public interface FHIRParticipantMapper {

    @Mapping(target = "id", source = "model.publicId")
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "actor", ignore = true)
    @Mapping(target = "period", source = "period")
    @Mapping(target = "required", source = "model.required")
    @Mapping(target = "status", source = "model.status")
    void map(@MappingTarget AppointmentParticipantComponent component, ParticipantModel model, PeriodModel period);

    void map(@MappingTarget AppointmentParticipantComponent participantComponent, ServiceProviderModel model);

    default List<CodeableConcept> map(@MappingTarget List<CodeableConcept> concepts, String value) {
	concepts.add(new CodeableConcept(new Coding(null, value, value)));

	return concepts;
    }

    default ParticipantRequired map(boolean required) {
	if (required) {
	    return ParticipantRequired.REQUIRED;
	} else {
	    return ParticipantRequired.OPTIONAL;
	}
    }
    
    default ParticipationStatus map(String status) {
	return ParticipationStatus.fromCode(status);
    }

    @Mapping(target = "start", source = "start")
    @Mapping(target = "end", source = "end")
    Period map(PeriodModel model);
}
