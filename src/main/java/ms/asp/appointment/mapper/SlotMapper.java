package ms.asp.appointment.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ms.asp.appointment.domain.Slot;
import ms.asp.appointment.model.AppointmentSlotModel;
import ms.asp.appointment.model.ServiceProviderSlotModel;

@Mapper(imports = { LocalDateTime.class, LocalDate.class })
public interface SlotMapper {

    @Mapping(target = "publicId", ignore = true)
    Slot appointmentSlotModelToSlot(AppointmentSlotModel slotModel);

    @InheritInverseConfiguration
    AppointmentSlotModel slotToAppointmentSlotModel(Slot slot);

    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "start", expression = "java(LocalDateTime.of(LocalDate.now(), slotModel.getStart()))")
    @Mapping(target = "end", expression = "java(LocalDateTime.of(LocalDate.now(), slotModel.getEnd()))")
    Slot serviceProviderSlotModelToSlot(ServiceProviderSlotModel slotModel);

    @Mapping(target = "start", expression = "java(slot.getStart().toLocalTime())")
    @Mapping(target = "end", expression = "java(slot.getEnd().toLocalTime())")
    ServiceProviderSlotModel slotToServiceProviderSlotModel(Slot slot);
}
