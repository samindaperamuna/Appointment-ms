package ms.asp.appointment.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ms.asp.appointment.domain.Participant;
import ms.asp.appointment.domain.ParticipantInfo;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.exception.ParticipantException;
import ms.asp.appointment.mapper.ParticipantMapper;
import ms.asp.appointment.model.ParticipantModel;
import ms.asp.appointment.repository.AppointmentRepository;
import ms.asp.appointment.repository.ParticipantRepository;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ParticipantService extends AbstractService<Participant, Long, ParticipantModel> {

    private final AppointmentRepository appointmentRepository;
    private final ParticipantInfoService participantInfoService;

    private static final NotFoundException NOT_FOUND = new NotFoundException("No participant found for that ID");
    private static final ParticipantException NO_APPOINTMENT = new ParticipantException("No appointment defined");

    public ParticipantService(
	    ParticipantRepository repository,
	    AppointmentRepository appointmentRepository,
	    ParticipantInfoService participantInfoService,
	    ParticipantMapper mapper) {

	super(repository, mapper);

	this.appointmentRepository = appointmentRepository;
	this.participantInfoService = participantInfoService;
    }

    public Mono<Participant> create(Participant participant) {
	return Mono.just(participant)
		.flatMap(p -> {
		    if (p.getAppointment() == null) {
			return Mono.error(NO_APPOINTMENT);
		    }

		    return Mono.just(p);
		})
		.flatMap(s -> save(s, false));
    }

    public Mono<Participant> update(Participant participant) {
	return Mono.just(participant)
		.flatMap(p -> {
		    if (p.getAppointment() == null) {
			return Mono.error(NO_APPOINTMENT);
		    }

		    return Mono.just(p);
		})
		// Get saved slot
		.flatMap(s -> repository.findByPublicId(s.getPublicId())
			.switchIfEmpty(Mono.error(NOT_FOUND))
			.map(r -> {
			    s.setId(r.getId());

			    return s;
			}))
		// Save the slot
		.flatMap(s -> save(s, true))
		// Fetch all including attached entities
		.flatMap(s -> findByPublicIdEager(s.getPublicId()));
    }

    public Mono<Participant> delete(Long id) {
	return repository.findById(id)
		.switchIfEmpty(Mono.error(NOT_FOUND))
		.flatMap(p -> Mono.just(p.getParticipantInfo())
			.flatMap(pi -> participantInfoService.delete(pi.getPublicId()))
			.then(Mono.just(p)))
		.flatMap(p -> repository.delete(p)
			.then(Mono.just(p)));
    }

    private Mono<Participant> save(Participant participant, boolean update) {
	return Mono.just(participant)
		.flatMap(p -> {
		    // Check if ID is already set
		    if (p.getAppointmentId() != null) {
			return Mono.just(p);
		    }

		    return appointmentRepository.findByPublicId(p.getAppointment().getPublicId())
			    .switchIfEmpty(Mono.error(new ParticipantException("No appointment found for that ID")))
			    .map(a -> {
				p.setAppointmentId(a.getId());

				return p;
			    });
		})
		.flatMap(repository::save)
		// Save participant info
		.flatMap(p -> {
		    if (p.getParticipantInfo() == null) {
			return Mono.just(p);
		    }

		    return Mono.just(p.getParticipantInfo())
			    .flatMap(pi -> switchUpdate(pi, update))
			    .then(Mono.just(p));
		});
    }

    protected Mono<Participant> findByPublicIdEager(String publicId) {
	return repository.findByPublicId(publicId)
		.switchIfEmpty(Mono.error(NOT_FOUND))
		// Get appointment
		.flatMap(p -> appointmentRepository.findById(p.getAppointmentId())
			.map(a -> {
			    p.setAppointment(a);

			    return p;
			}))
		// Get participant info
		.flatMap(p -> participantInfoService.findByIdEager(p.getId())
			.map(pi -> {
			    p.setParticipantInfo(pi);

			    return p;
			}));
    }

    private Mono<ParticipantInfo> switchUpdate(ParticipantInfo participantInfo, boolean update) {
	if (update) {
	    return participantInfoService.update(participantInfo);
	} else {
	    return participantInfoService.create(participantInfo);
	}
    }
}
