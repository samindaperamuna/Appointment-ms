package ms.asp.appointment.service;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ms.asp.appointment.domain.Appointment;
import ms.asp.appointment.domain.AppointmentHistory;
import ms.asp.appointment.domain.Contact;
import ms.asp.appointment.domain.ParticipantInfo;
import ms.asp.appointment.exception.AppointmentException;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.mapper.AppointmentHistoryMapper;
import ms.asp.appointment.mapper.AppointmentMapper;
import ms.asp.appointment.mapper.PeriodMapper;
import ms.asp.appointment.model.AppointmentModel;
import ms.asp.appointment.model.PeriodModel;
import ms.asp.appointment.repository.AppointmentHistoryRepository;
import ms.asp.appointment.repository.AppointmentNoteRepository;
import ms.asp.appointment.repository.AppointmentRepository;
import ms.asp.appointment.repository.BaseRepository;
import ms.asp.appointment.repository.ContactRepository;
import ms.asp.appointment.repository.NoteRepository;
import ms.asp.appointment.repository.ParticipantInfoRepository;
import ms.asp.appointment.repository.ParticipantRepository;
import ms.asp.appointment.repository.PeriodRepository;
import ms.asp.appointment.repository.ServiceProviderRepository;
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
    private final ServiceProviderRepository serviceProviderRepository;
    private final PeriodRepository periodRepository;

    private final PeriodMapper periodMapper;
    private final AppointmentHistoryMapper historyMapper;

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
	    ServiceProviderRepository serviceProviderRepository,
	    PeriodRepository periodRepository,
	    AppointmentMapper mapper,
	    PeriodMapper periodMapper,
	    AppointmentHistoryMapper historyMapper) {

	super(repository, mapper);

	this.historyRepository = historyRepository;
	this.noteRepository = noteRepository;
	this.appointmentNoteRepository = appoinmentNoteRepository;
	this.participantRepository = participantRepository;
	this.participantInfoRepository = participantInfoRepository;
	this.contactRepository = contactRepository;
	this.serviceProviderRepository = serviceProviderRepository;
	this.periodRepository = periodRepository;
	this.periodMapper = periodMapper;
	this.historyMapper = historyMapper;
    }

    public Mono<Page<AppointmentModel>> findByPage(PageRequest pageRequest) {
	return this.repository.findBy(pageRequest)
		.flatMap(a -> findByPublicId(a.getPublicId()))
		.map(mapper::toModel)
		.collectList()
		.zipWith(this.repository.count())
		.map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    public Mono<AppointmentModel> findOne(String publicId) {
	return findByPublicId(publicId)
		.map(mapper::toModel);
    }

    public Mono<AppointmentModel> create(AppointmentModel model) {
	var appointment = mapper.toEntity(model);

	return save(appointment, false)
		// Fetch all including attached entities
		.flatMap(p -> findByPublicId(p.getPublicId()))
		.map(mapper::toModel);
    }

    public Mono<AppointmentModel> update(AppointmentModel model) {

	return Mono.just(mapper.toEntity(model))
		// Get saved appointment
		.flatMap(a -> repository.findByPublicId(a.getPublicId())
			.switchIfEmpty(Mono.error(new NotFoundException("No appointment found for that ID")))
			.map(appointment -> {
			    a.setId(appointment.getId());
			    a.setVersion(appointment.getVersion());
			    a.setCreated(appointment.getCreated());
			    a.setModified(appointment.getModified());

			    return a;
			}))
		// Get saved service provider
		.flatMap(a -> {
		    if (a.getServiceProvider() == null || a.getServiceProvider().getPublicId() == null
			    || a.getServiceProvider().getPublicId().isBlank())
			return Mono.just(a);

		    return serviceProviderRepository.findByPublicId(a.getServiceProvider().getPublicId())
			    .switchIfEmpty(Mono.error(new NotFoundException("No service provider found for that ID")))
			    .map(p -> {
				a.setServiceProviderId(p.getId());

				return a;
			    });
		})
		// Get saved notes
		.flatMap(a -> {
		    if (a.getNotes() == null || a.getNotes().isEmpty())
			return Mono.just(a);

		    return Mono.just(a.getNotes())
			    .flatMapMany(Flux::fromIterable)
			    .map(n -> noteRepository.findByPublicId(n.getPublicId())
				    .map(note -> {
					n.setId(note.getId());

					return n;
				    }))
			    .collect(Collectors.toSet())
			    .then(Mono.just(a));
		})
		// Get saved participants
		.flatMap(a -> {
		    if (a.getParticipants() == null || a.getParticipants().isEmpty())
			return Mono.just(a);

		    return Mono.just(a.getParticipants())
			    .flatMapMany(Flux::fromIterable)
			    .flatMap(p -> participantRepository.findByPublicId(p.getPublicId())
				    .onErrorComplete()
				    .map(participant -> {
					p.setId(participant.getId());

					return p;
				    }))
			    .flatMap(p -> participantInfoRepository
				    .findByPublicId(p.getParticipantInfo().getPublicId())
				    .onErrorComplete()
				    .map(pInfo -> {
					p.setParticipantInfoId(pInfo.getId());
					p.getParticipantInfo().setId(pInfo.getId());

					return p;
				    }))
			    .flatMap(p -> contactRepository
				    .findByPublicId(p.getParticipantInfo().getContact().getPublicId())
				    .onErrorComplete()
				    .map(contact -> {
					p.getParticipantInfo().getContact().setId(contact.getId());
					p.getParticipantInfo().setContactId(contact.getId());

					return p;
				    }))
			    .collect(Collectors.toSet())
			    .then(Mono.just(a));
		})
		// Get saved period
		.flatMap(a -> {
		    if (a.getPeriod() == null || a.getPeriod().getPublicId() == null
			    || a.getPeriod().getPublicId().isBlank())
			return Mono.just(a);

		    return periodRepository.findByPublicId(a.getPeriod().getPublicId())
			    .switchIfEmpty(Mono.error(new NotFoundException("No period found for that ID")))
			    .map(p -> {
				a.getPeriod().setId(p.getId());
				a.setPeriodId(p.getId());

				return a;
			    });
		})
		// Save appointment history
		.flatMap(a -> findByPublicId(a.getPublicId())
			.flatMap(old -> {
			    var history = historyMapper.toHistory(old);
			    history.setId(null);

			    return historyRepository.save(history).then(Mono.just(a));
			}))
		// Save the appointment
		.flatMap(a -> save(a, true))
		// Fetch all including attached entities
		.flatMap(p -> findByPublicId(p.getPublicId()))
		.map(mapper::toModel);
    }

    public Mono<AppointmentModel> delete(String publicId) {
	return repository.findByPublicId(publicId)
		// Save appointment history
		.flatMap(a -> findByPublicId(a.getPublicId())
			.switchIfEmpty(Mono.error(new NotFoundException("No Appointement has id = " + publicId)))
			.flatMap(old -> {
			    var history = historyMapper.toHistory(old);
			    history.setId(null);
			    history.setStatus("DELETED");

			    return historyRepository.save(history).then(Mono.just(a));
			}))
		// Delete appointment
		.flatMap(a -> repository.deleteByPublicId(a.getPublicId())
			.then(Mono.just(a))
			.map(mapper::toModel));

    }

    /**
     * Reschedule an appointment. The version is updated and a revision in the history table is created.
     * 
     * @param publicId
     * @param periodModel Time range information
     * @return
     */
    public Mono<AppointmentModel> reschedule(String publicId, @Valid PeriodModel periodModel) {
	return repository.findByPublicId(publicId)
		.switchIfEmpty(Mono.error(new NotFoundException("No Appointement has id = " + publicId)))
		// Save history
		.flatMap(old -> historyRepository.save(historyMapper.toHistory(old))
			.then(Mono.just(old)))
		// Update period
		.map(a -> {
		    a.setPeriod(periodMapper.toEntity(periodModel));

		    return a;
		})
		// Save the appointment
		.flatMap(a -> save(a, true))
		.map(mapper::toModel);
    }

    /**
     * Get appointment history by public ID. Returns all revisions per specific interview ID from the history table.
     * 
     * @param publicId public id of the interview.
     * @return {@link Flux<AppointmentHistory}
     */
    public Flux<AppointmentHistory> history(String publicId) {

	return historyRepository.findRevisions(publicId)
		.switchIfEmpty(Flux.error(new NotFoundException("No history found for that ID")));
    }

    /**
     * Save without incrementing the version. No history is created.
     * 
     * @param appointment
     * @return
     */
    private Mono<Appointment> save(Appointment appointment, boolean update) {

	return Mono.just(appointment)
		// Save service provider if exists else throw an error
		.flatMap(a -> {
		    if (a.getServiceProvider() == null || a.getServiceProvider().getPublicId() == null) {
			// If its update and no service provider, return
			if (update) {
			    return Mono.just(a);
			} else {
			    return Mono.error(new AppointmentException("No service provider defined"));
			}
		    }

		    return serviceProviderRepository.findByPublicId(a.getServiceProvider().getPublicId())
			    .map(s -> {
				a.setServiceProviderId(s.getId());

				return a;
			    })
			    .switchIfEmpty(Mono.error(new NotFoundException("Service provider not found!")))
			    .then(Mono.just(a));
		})
		// Save period if exists else throw an error
		.flatMap(a -> {
		    if (a.getPeriod() == null) {
			// If its update and no period, return
			if (update) {
			    return Mono.just(a);
			} else {
			    return Mono.error(new AppointmentException("No period defined"));
			}
		    }

		    return periodRepository.save(a.getPeriod())
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
			// If its update, return
			if (update) {
			    return Mono.just(a);
			} else {
			    return Mono.error(new AppointmentException("No participants are defined"));
			}
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

				    if (contact.getEmail() != null || contact.getEmail() != null) {
					return contactRepository.save(contact)
						.map(c -> {
						    p.getParticipantInfo().getContact().setId(c.getId());
						    p.getParticipantInfo().setContactId(c.getId());

						    return p;
						});
				    }
				}

				return Mono.just(p);
			    })
			    // Save participant info and set participant id
			    .flatMap(p -> {
				if (p.getParticipantInfo() != null) {
				    ParticipantInfo pInfo = p.getParticipantInfo();

				    return participantInfoRepository.save(pInfo)
					    .map(pi -> {
						p.getParticipantInfo().setId(pi.getId());
						p.setParticipantInfoId(pi.getId());

						return p;
					    });
				}

				return Mono.just(p);
			    })
			    // Save participant
			    .flatMap(p -> participantRepository.save(p))
			    // Merge all flux operations
			    .collect(Collectors.toSet())
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
		// Set service provider and return
		.flatMap(a -> serviceProviderRepository.findById(a.getServiceProviderId())
			.map(p -> {
			    a.setServiceProvider(p);
			    a.setServiceProviderId(p.getId());

			    return p;
			})
			// Set service provider contact and return
			.flatMap(p -> contactRepository.findById(p.getContactId())
				.map(c -> {
				    p.setContact(c);
				    p.setContactId(c.getId());

				    return a;
				})))
		// Set period information and return
		.flatMap(a -> {
		    if (a.getPeriodId() == null) {
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
			    .collectList()
			    .map(notes -> {
				a.setNotes(notes);

				return a;
			    });
		})
		// Set participants and return
		.flatMap(a -> {
		    return participantRepository.findByAppointmentId(a.getId())
			    .flatMap(p -> {
				if (p.getParticipantInfoId() == null) {
				    return Mono.just(p);
				}

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
			    .collectList()
			    .map(parts -> {
				a.setParticipants(parts);

				return a;
			    });
		});
    }
}
