package ms.asp.appointment.service;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ms.asp.appointment.domain.Appointment;
import ms.asp.appointment.domain.AppointmentHistory;
import ms.asp.appointment.domain.Contact;
import ms.asp.appointment.domain.ParticipantInfo;
import ms.asp.appointment.exception.AppointmentException;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.mapper.AppointmentMapper;
import ms.asp.appointment.model.AppointmentModel;
import ms.asp.appointment.model.PeriodModel;
import ms.asp.appointment.repository.AppointmentHistoryRepository;
import ms.asp.appointment.repository.AppointmentNoteRepository;
import ms.asp.appointment.repository.AppointmentRepository;
import ms.asp.appointment.repository.AppointmentSlotRepository;
import ms.asp.appointment.repository.BaseRepository;
import ms.asp.appointment.repository.ContactRepository;
import ms.asp.appointment.repository.NoteRepository;
import ms.asp.appointment.repository.ParticipantInfoRepository;
import ms.asp.appointment.repository.ParticipantRepository;
import ms.asp.appointment.repository.PeriodRepository;
import ms.asp.appointment.repository.ServiceProviderRepository;
import ms.asp.appointment.repository.SlotRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
@Slf4j
public class AppointmentService extends AbstractService<Appointment, Long, AppointmentModel> {

    private final AppointmentHistoryRepository historyRepository;
    private final NoteRepository noteRepository;
    private final AppointmentNoteRepository appointmentNoteRepository;
    private final ParticipantRepository participantRepository;
    private final ParticipantInfoRepository participantInfoRepository;
    private final ContactRepository contactRepository;
    private final SlotRepository slotRepository;
    private final AppointmentSlotRepository appoinmentSlotRepository;
    private final ServiceProviderRepository serviceProviderRepository;
    private final PeriodRepository periodRepository;

    /**
     * Pass in the repository and the mapper to the super class.
     * 
     * @param repository        Implementation of {@link BaseRepository} to pass to the super class
     * @param historyRepository
     * @param mapper            Implementation of {@link Mapper} to pass to the super class/
     */
    public AppointmentService(AppointmentRepository repository,
	    AppointmentHistoryRepository historyRepository,
	    NoteRepository noteRepository,
	    AppointmentNoteRepository appoinmentNoteRepository,
	    ParticipantRepository participantRepository,
	    ParticipantInfoRepository participantInfoRepository,
	    ContactRepository contactRepository,
	    SlotRepository slotRepository,
	    AppointmentSlotRepository appointmentSlotRepository,
	    ServiceProviderRepository serviceProviderRepository,
	    PeriodRepository periodRepository,
	    AppointmentMapper mapper) {

	super(repository, mapper);

	this.historyRepository = historyRepository;
	this.noteRepository = noteRepository;
	this.appointmentNoteRepository = appoinmentNoteRepository;
	this.participantRepository = participantRepository;
	this.participantInfoRepository = participantInfoRepository;
	this.contactRepository = contactRepository;
	this.slotRepository = slotRepository;
	this.appoinmentSlotRepository = appointmentSlotRepository;
	this.serviceProviderRepository = serviceProviderRepository;
	this.periodRepository = periodRepository;
    }

    public Mono<AppointmentModel> create(AppointmentModel model) {
	var appointment = mapper.toEntity(model);

	return save(appointment)
		.map(mapper::toModel);
    }

    public Mono<AppointmentModel> findOne(String publicId) {
	return findByPublicId(publicId)
		.map(mapper::toModel);
    }

    public Mono<AppointmentModel> update(AppointmentModel model) {

	return save(mapper.toEntity(model))
		.flatMap(a -> repository.save(a))
		.flatMap(a -> historyRepository.save(((AppointmentMapper) mapper).toHistory(a))
			.then(Mono.just(a)))
		.map(mapper::toModel);
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

		    return a;
		})
		.switchIfEmpty(Mono.error(e))
		.map(mapper::toModel);
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
    private Mono<Appointment> save(Appointment appointment) {

	return Mono.just(appointment)
		// Save service provider if exists else throw an error
		.flatMap(a -> {
		    if (a.getServiceProvider() == null) {
			return Mono.error(new AppointmentException("No service provider defined"));
		    } else if (a.getServiceProvider().getPublicId() == null) {
			return Mono
				.error(new AppointmentException("A reference to existing service provider required"));
		    }

		    return serviceProviderRepository.findByPublicId(a.getServiceProvider().getPublicId())
			    .map(s -> {
				a.setServiceProviderId(s.getId());

				return a;
			    })
			    .switchIfEmpty(Mono.error(new AppointmentException("Service provider not found!")))
			    .then(Mono.just(a));
		})
		// Save period if exists
		.flatMap(a -> {
		    if (a.getPeriod() == null) {
			return Mono.just(a);
		    }

		    return periodRepository.save(a.getPeriod())
			    .log()
			    .map(p -> {
				a.setPeriodId(p.getId());

				return a;
			    }).then(Mono.just(a));
		})
		.flatMap(a -> repository.save(a))
		.onErrorComplete(e -> {
		    log.error("Failed to save appointment: " + e.getLocalizedMessage());

		    return false;
		})
		// Save participants if exists else throw an error
		.flatMap(a -> {
		    if (a.getParticipants() == null) {
			return Mono.error(new AppointmentException("No participants are defined"));
		    }

		    return Mono.just(a.getParticipants())
			    .flatMapMany(Flux::fromIterable)
			    .map(p -> {
				p.setAppointmentId(a.getId());

				return p;
			    })
			    // Save contact and set contact id
			    .flatMap(p -> {
				if (p.getParticipantInfo() != null && p.getParticipantInfo().getContact() != null) {
				    Contact contact = p.getParticipantInfo().getContact();

				    return contactRepository.save(contact)
					    .map(c -> {
						p.getParticipantInfo().setContactId(c.getId());

						return p;
					    });
				}

				return Mono.just(p);
			    })
			    // Save participant info and set participant id
			    .flatMap(p -> {
				if (p.getParticipantInfo() != null) {
				    ParticipantInfo participantInfo = p.getParticipantInfo();

				    return participantInfoRepository.save(participantInfo)
					    .map(pi -> {
						p.setParticipantInfoId(pi.getId());

						return p;
					    });
				}

				return Mono.just(p);
			    })
			    // Save participant
			    .flatMap(p -> participantRepository.save(p))
			    .then(Mono.just(a));
		})
		// Save slots if exists else throw an error
		.flatMap(a -> {
		    if (a.getSlots() == null) {
			return Mono.error(new AppointmentException("No slots are defined"));
		    }

		    return Mono.just(a.getSlots())
			    .flatMapMany(Flux::fromIterable)
			    .flatMap(s -> slotRepository.save(s))
			    .flatMap(s -> appoinmentSlotRepository.saveAppointmentSlot(a, s))
			    .then(Mono.just(a));
		})
		// Save notes if exists
		.flatMap(a -> {
		    if (a.getNotes() == null) {
			return Mono.just(a);
		    }

		    return Mono.just(a.getNotes())
			    .flatMapMany(Flux::fromIterable)
			    .flatMap(n -> noteRepository.save(n))
			    .flatMap(n -> appointmentNoteRepository.saveAppointmentNote(a, n))
			    .then(Mono.just(a));
		});
    }

    public Mono<Appointment> findByPublicId(String publicId) {
	return repository.findByPublicId(publicId)
		.switchIfEmpty(Mono.error(new NotFoundException("No appointment found for that ID")))
		// Set period information and return
		.flatMap(a -> {
		    if (a.getPeriod() == null) {
			return Mono.just(a);
		    }

		    return periodRepository.findById(a.getPeriodId())
			    .map(p -> {
				a.setPeriod(p);

				return a;
			    });
		})
		// Set notes and return
		.flatMap(a -> {
		    return appointmentNoteRepository.findNotes(a, 5)
			    .collect(Collectors.toSet())
			    .map(notes -> {
				a.setNotes(notes);

				return a;
			    });
		})
		// Set slots and return
		.flatMap(a -> {
		    // Set slots
		    return appoinmentSlotRepository.findSlots(a, 5)
			    .collect(Collectors.toSet())
			    .map(slots -> {
				a.setSlots(slots);

				return a;
			    });
		})
		// Set participants and return
		.flatMap(a -> {
		    return participantRepository.findByAppointmentId(a.getId())
			    .flatMap(p -> {
				return participantInfoRepository.findById(p.getParticipantInfoId())
					.flatMap(pi -> {
					    if (pi.getContactId() == null)
						return Mono.just(pi);

					    return contactRepository.findById(pi.getContactId())
						    .map(c -> {
							pi.setContact(c);

							return pi;
						    });
					})
					.map(pi -> {
					    p.setParticipantInfo(pi);

					    return p;
					});
			    })
			    .collect(Collectors.toSet())
			    .map(parts -> {
				a.setParticipants(parts);

				return a;
			    });
		});
    }
}
