package ms.asp.appointment.service;

import static ms.asp.appointment.util.CommonUtils.generatePublicId;

import javax.validation.Valid;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ms.asp.appointment.domain.Appointment;
import ms.asp.appointment.domain.AppointmentHistory;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.mapper.AppointmentMapper;
import ms.asp.appointment.model.AppointmentModel;
import ms.asp.appointment.model.PeriodModel;
import ms.asp.appointment.repository.AppointmentHistoryRepository;
import ms.asp.appointment.repository.AppointmentRepository;
import ms.asp.appointment.repository.BaseRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AppointmentService extends AbstractService<Appointment, Long, AppointmentModel> {

    private final AppointmentHistoryRepository historyRepository;

    /**
     * Pass in the repository and the mapper to the super class.
     * 
     * @param repository        Implementation of {@link BaseRepository} to pass to the super class
     * @param historyRepository
     * @param mapper            Implementation of {@link Mapper} to pass to the super class/
     */
    public AppointmentService(AppointmentRepository repository, AppointmentHistoryRepository historyRepository,
	    AppointmentMapper mapper) {
	super(repository, mapper);

	this.historyRepository = historyRepository;
    }

    public Mono<AppointmentModel> create(AppointmentModel model) {
	var appointment = mapper.toEntity(model);
	appointment.setPublicId(generatePublicId());

	return save(appointment);
    }

    public Mono<AppointmentModel> update(AppointmentModel model) {
	return updateVersion(mapper.toEntity(model));
    }

    public Mono<AppointmentModel> delete(String publicId) {
	var e = new NotFoundException("No Appointement has id = " + publicId);

	repository.findByPublicId(publicId)
		.map(a -> {
		    return historyRepository.save(((AppointmentMapper) mapper).toHistory(a));
		})
		.switchIfEmpty(Mono.error(e))
		.subscribe();

	return repository.deleteByPublicId(publicId)
		.switchIfEmpty(Mono.error(e))
		.map(mapper::toModel);
    }

    /**
     * Reschedule an appointment. The version is updated and a revision in the history table is created.
     * 
     * @param publicId
     * @param periodModel Time range information
     * @return
     */
    public Mono<AppointmentModel> reschedule(String publicId, @Valid PeriodModel periodModel) {
	var e = new NotFoundException("No Appointement has id = " + publicId);

	return repository.findByPublicId(publicId)
		.map(a -> {
		    a.setStart(periodModel.getStart());
		    a.setEnd(periodModel.getEnd());

		    updateVersion(a);

		    return a;
		})
		.map(mapper::toModel)
		.switchIfEmpty(Mono.error(e));
    }

    /**
     * Get appointment history by public ID. Returns all revisions per specific interview ID from the history table.
     * 
     * @param publicId public id of the interview.
     * @return {@link Flux<AppointmentHistory}
     */
    public Flux<AppointmentHistory> history(String publicId) {
	var e = new NotFoundException("No Appointement with id: " + publicId);

	Mono<Long> entityId = repository.findByPublicId(publicId)
		.map(Appointment::getId);

	return historyRepository.findRevisions(entityId)
		.switchIfEmpty(Mono.error(e));
    }

    /**
     * Save without incrementing the version. No history is created.
     * 
     * @param appointment
     * @return
     */
    private Mono<AppointmentModel> save(Appointment appointment) {
	return ((AppointmentRepository) repository).save(appointment)
		.map(mapper::toModel);
    }

    /**
     * Increment version for the entity and save. Saves a copy in the history table.
     * 
     * @param appointment
     * @return
     */
    private Mono<AppointmentModel> updateVersion(Appointment appointment) {
	appointment.setVersion(appointment.getVersion() + 1);

	historyRepository.save(((AppointmentMapper) mapper).toHistory(appointment))
		.subscribe();

	return repository.save(appointment)
		.map(mapper::toModel);
    }
}
