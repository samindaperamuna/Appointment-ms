package ms.asp.appointment.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ms.asp.appointment.domain.AppointmentFlow;
import ms.asp.appointment.domain.ServiceType;
import ms.asp.appointment.model.AppointmentFlowModel;
import ms.asp.appointment.util.JSONUtils;

@Mapper(config = BaseMapper.class)
public interface AppointmentFlowMapper extends BaseMapper<AppointmentFlow, AppointmentFlowModel> {

    @Mapping(source = "serviceTypeJSON", target = "serviceTypes")
    AppointmentFlowModel toModel(AppointmentFlow flow);

    default List<ServiceType> jsonToServiceTypes(String json) {
	return JSONUtils.jsonToServiceType(json);
    }
}
