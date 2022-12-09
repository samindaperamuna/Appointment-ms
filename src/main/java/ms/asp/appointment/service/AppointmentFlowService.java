package ms.asp.appointment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ms.asp.appointment.domain.AppointmentFlow;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.exception.ServiceProviderException;
import ms.asp.appointment.mapper.AppointmentFlowMapper;
import ms.asp.appointment.model.AppointmentFlowModel;
import ms.asp.appointment.repository.AppointmentFlowRepository;
import ms.asp.appointment.util.JSONUtils;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AppointmentFlowService extends AbstractService<AppointmentFlow, Long, AppointmentFlowModel> {

    public AppointmentFlowService(AppointmentFlowRepository repository,
	    AppointmentFlowMapper mapper) {

	super(repository, mapper);
    }

    public Mono<Page<AppointmentFlowModel>> findByPage(PageRequest pageRequest) {
	return this.repository.findBy(pageRequest)
		.map(mapper::toModel)
		.collectList()
		.zipWith(this.repository.count())
		.map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    public Mono<AppointmentFlowModel> findOne(String publicId) {
	return findByPublicId(publicId)
		.map(mapper::toModel);
    }
    
    public Mono<AppointmentFlowModel> create(AppointmentFlowModel model) {
	var appointmentFlow = mapper.toEntity(model);

	return save(appointmentFlow, false)
		.map(mapper::toModel);
    }

    public Mono<AppointmentFlowModel> update(AppointmentFlowModel model) {
	return Mono.just(mapper.toEntity(model))
		// Get saved appointment flow
		.flatMap(f -> repository.findByPublicId(f.getPublicId())
			.switchIfEmpty(Mono.error(new NotFoundException("No appointment flow found for that ID")))
			.map(r -> {
			    f.setId(r.getId());
			    f.setCreated(r.getCreated());
			    f.setModified(r.getModified());

			    return f;
			}))
		// Save the appointment flow
		.flatMap(f -> save(f, true))
		.map(mapper::toModel);
    }

    public Mono<AppointmentFlowModel> delete(String publicId) {
	return findByPublicId(publicId)
		.switchIfEmpty(Mono.error(new NotFoundException("No appointment flow found for that ID")))
		.flatMap(f -> {
		    return repository.delete(f)
			    .then(Mono.just(f));
		}).map(mapper::toModel);
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
			    return Mono.error(new ServiceProviderException("Service types not provided"));
			}
		    }

		    f.setServiceTypeJSON(JSONUtils.serviceTypeToJSON(f.getServiceTypes()));

		    return Mono.just(f);
		})
		.flatMap(repository::save);
    }

    private Mono<AppointmentFlow> findByPublicId(String publicId) {
	return repository.findByPublicId(publicId)
		.switchIfEmpty(Mono.error(new NotFoundException("No appointment flow found for that ID")))
		// Parse and set service types
		.map(f -> {
		    if (f.getServiceTypes() != null || !f.getServiceTypeJSON().isBlank())
			f.setServiceTypes(JSONUtils.jsonToServiceType(f.getServiceTypeJSON()));

		    return f;
		});
    }
}
