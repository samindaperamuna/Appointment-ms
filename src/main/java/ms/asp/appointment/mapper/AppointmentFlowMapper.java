package ms.asp.appointment.mapper;

import org.mapstruct.Mapper;

import ms.asp.appointment.domain.AppointmentFlow;
import ms.asp.appointment.model.AppointmentFlowModel;

@Mapper
public interface AppointmentFlowMapper extends BaseMapper<AppointmentFlow, AppointmentFlowModel> {
}
