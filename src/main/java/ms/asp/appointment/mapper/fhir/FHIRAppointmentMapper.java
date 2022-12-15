package ms.asp.appointment.mapper.fhir;

import java.util.List;

import org.hl7.fhir.r4.model.Appointment;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Period;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ms.asp.appointment.domain.AppointmentType;
import ms.asp.appointment.domain.Reason;
import ms.asp.appointment.domain.ServiceCategory;
import ms.asp.appointment.domain.ServiceType;
import ms.asp.appointment.domain.Speciality;
import ms.asp.appointment.model.AppointmentModel;
import ms.asp.appointment.model.PeriodModel;

@Mapper(uses = { FHIRParticipantMapper.class })
public interface FHIRAppointmentMapper {

    @Mapping(target = "id", source = "publicId")
    @Mapping(target = "serviceType", ignore = true)
    @Mapping(target = "start", source = "requestedPeriod.start")
    @Mapping(target = "end", source = "requestedPeriod.end")
    @Mapping(target = "supportingInformation", ignore = true)
    Appointment get(AppointmentModel model);

    default List<CodeableConcept> map(ServiceCategory serviceCategory) {
	return List.of(new CodeableConcept()
		.addCoding(new Coding(null, serviceCategory.toString(), serviceCategory.getValue())));
    }

    default List<CodeableConcept> map(ServiceType serviceType) {
	return List.of(new CodeableConcept()
		.addCoding(new Coding(null, serviceType.toString(), serviceType.getValue())));
    }

    default List<CodeableConcept> map(Speciality speciality) {
	return List.of(new CodeableConcept()
		.addCoding(new Coding(null, speciality.toString(), speciality.getValue())));
    }

    default List<CodeableConcept> map(AppointmentType appointmentType) {
	return List.of(new CodeableConcept()
		.addCoding(new Coding(null, appointmentType.toString(), appointmentType.getValue())));
    }

    default List<Period> map(PeriodModel periodModel) {
	return List.of(new Period().setStart(java.sql.Timestamp.valueOf(periodModel.getStart()))
		.setEnd(java.sql.Timestamp.valueOf(periodModel.getEnd())));
    }

    default List<CodeableConcept> map(Reason reasonCode) {
	return List.of(new CodeableConcept().addCoding(new Coding(reasonCode.getSystem(),
		reasonCode.getCode(),
		reasonCode.getDisplay())));
    }
}