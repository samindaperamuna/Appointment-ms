package ms.asp.appointment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ms.asp.appointment.domain.AppointmentFlow;
import ms.asp.appointment.exception.AppointmentFlowException;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.mapper.AppointmentFlowMapper;
import ms.asp.appointment.model.AppointmentFlowModel;
import ms.asp.appointment.repository.AppointmentFlowRepository;
import ms.asp.appointment.util.JSONUtils;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AppointmentFlowService extends AbstractService<AppointmentFlow, Long, AppointmentFlowModel> {

    private static final NotFoundException NOT_FOUND = new NotFoundException("No appointment flow found for that ID");

    public AppointmentFlowService(AppointmentFlowRepository repository,
	    AppointmentFlowMapper mapper) {

	super(repository, mapper);
    }

    public Mono<AppointmentFlow> create(AppointmentFlow appointmentFlow) {
	return save(appointmentFlow, false);
    }

    public Mono<AppointmentFlow> update(AppointmentFlow appointmentFlow) {
	return Mono.just(appointmentFlow)
		// Get saved appointment flow
		.flatMap(f -> repository.findByPublicId(f.getPublicId())
			.switchIfEmpty(Mono.error(NOT_FOUND))
			.map(r -> {
			    f.setId(r.getId());
			    f.setCreated(r.getCreated());
			    f.setModified(r.getModified());

			    return f;
			}))
		// Save the appointment flow
		.flatMap(f -> save(f, true));
    }

    public Mono<AppointmentFlow> delete(Long id) {
	return repository.findById(id)
		.switchIfEmpty(Mono.error(NOT_FOUND))
		.flatMap(f -> {
		    return repository.delete(f)
			    .then(Mono.just(f));
		});
    }

    private Mono<AppointmentFlow> save(AppointmentFlow appointmentFlow, boolean update) {
	return Mono.just(appointmentFlow)
		// Save service types
		.flatMap(f -> {
		    if (f.getServiceTypes() == null || f.getServiceTypes().isEmpty()) {
			// If its update and no service types return
			if (update) {
			    return Mono.just(f);
			} else {
			    return Mono.error(new AppointmentFlowException("Service types not provided"));
			}
		    }

		    f.setServiceTypeJSON(JSONUtils.serviceTypeToJSON(f.getServiceTypes()));

		    return Mono.just(f);
		})
		.flatMap(repository::save);
    }

    protected Mono<AppointmentFlow> findByPublicIdEager(String publicId) {
	return repository.findByPublicId(publicId)
		.switchIfEmpty(Mono.error(NOT_FOUND))
		// Parse and set service types
		.map(f -> {
		    if (f.getServiceTypes() != null || !f.getServiceTypeJSON().isBlank())
			f.setServiceTypes(JSONUtils.jsonToServiceType(f.getServiceTypeJSON()));

		    return f;
		});
    }
}
