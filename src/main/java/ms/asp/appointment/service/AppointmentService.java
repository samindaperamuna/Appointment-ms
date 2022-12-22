package ms.asp.appointment.service;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.mapstruct.Mapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import lombok.extern.slf4j.Slf4j;
import ms.asp.appointment.domain.Appointment;
import ms.asp.appointment.domain.AppointmentHistory;
import ms.asp.appointment.exception.AppointmentException;
import ms.asp.appointment.exception.CalendarGenerationException;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.mapper.AppointmentHistoryMapper;
import ms.asp.appointment.mapper.AppointmentMapper;
import ms.asp.appointment.mapper.PeriodMapper;
import ms.asp.appointment.mapper.fhir.FHIRAppointmentMapper;
import ms.asp.appointment.model.PeriodModel;
import ms.asp.appointment.model.appointment.AppointmentModel;
import ms.asp.appointment.repository.AppointmentHistoryRepository;
import ms.asp.appointment.repository.AppointmentNoteRepository;
import ms.asp.appointment.repository.AppointmentRepository;
import ms.asp.appointment.repository.BaseRepository;
import ms.asp.appointment.repository.NoteRepository;
import ms.asp.appointment.repository.ParticipantRepository;
import ms.asp.appointment.repository.PeriodRepository;
import ms.asp.appointment.repository.ServiceProviderRepository;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ParameterList;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
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
    private final ParticipantService participantService;
    private final ServiceProviderRepository serviceProviderRepository;
    private final ServiceProviderService serviceProviderService;
    private final PeriodRepository periodRepository;

    private final PeriodMapper periodMapper;
    private final AppointmentHistoryMapper historyMapper;
    private final FHIRAppointmentMapper fhirMapper;

    private static final NotFoundException NOT_FOUND = new NotFoundException("No appointment found for that ID");

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
	    ParticipantService participantService,
	    ServiceProviderRepository serviceProviderRepository,
	    ServiceProviderService serviceProviderService,
	    PeriodRepository periodRepository,
	    AppointmentMapper mapper,
	    PeriodMapper periodMapper,
	    AppointmentHistoryMapper historyMapper,
	    FHIRAppointmentMapper fhirMapper) {

	super(repository, mapper);

	this.historyRepository = historyRepository;
	this.noteRepository = noteRepository;
	this.appointmentNoteRepository = appoinmentNoteRepository;
	this.participantRepository = participantRepository;
	this.participantService = participantService;
	this.serviceProviderRepository = serviceProviderRepository;
	this.serviceProviderService = serviceProviderService;
	this.periodRepository = periodRepository;
	this.periodMapper = periodMapper;
	this.historyMapper = historyMapper;
	this.fhirMapper = fhirMapper;
    }

    public Mono<Appointment> create(Appointment appointment) {
	return save(appointment, false)
		// Fetch all including attached entities
		.flatMap(p -> findByPublicIdEager(p.getPublicId()));
    }

    public Mono<Appointment> update(Appointment appointment) {
	return Mono.just(appointment)
		// Get saved appointment
		.flatMap(a -> repository.findByPublicId(a.getPublicId())
			.switchIfEmpty(Mono.error(NOT_FOUND))
			.map(r -> {
			    a.setId(r.getId());
			    a.setVersion(r.getVersion());
			    a.setCreated(r.getCreated());
			    a.setModified(r.getModified());

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
		.flatMap(a -> findByPublicIdEager(a.getPublicId())
			.flatMap(old -> {
			    var history = historyMapper.toHistory(old);
			    history.setId(null);

			    return historyRepository.save(history).then(Mono.just(a));
			}))
		// Save the appointment
		.flatMap(a -> save(a, true))
		// Fetch all including attached entities
		.flatMap(p -> findByPublicIdEager(p.getPublicId()));
    }

    public Mono<Appointment> delete(Long id) {
	return repository.findById(id)
		.switchIfEmpty(Mono.error(NOT_FOUND))
		// Save appointment history
		.flatMap(a -> findByPublicIdEager(a.getPublicId())
			.flatMap(old -> {
			    var history = historyMapper.toHistory(old);
			    history.setId(null);
			    history.setStatus("DELETED");

			    return historyRepository.save(history).then(Mono.just(a));
			}))
		// Delete participants
		.flatMap(a -> participantRepository.findByAppointmentId(a.getId())
			.flatMap(p -> participantService.delete(p.getId()))
			.collectList()
			.then(Mono.just(a)))
		// Delete appointment
		.flatMap(a -> repository.deleteByPublicId(a.getPublicId())
			.then(Mono.just(a)));
    }

    /**
     * Reschedule an appointment. The version is updated and a revision in the history table is created.
     * 
     * @param publicId
     * @param periodModel Time range information
     * @return
     */
    public Mono<AppointmentModel> reschedule(String publicId, @Valid PeriodModel periodModel) {
	return findByPublicIdEager(publicId)
		.switchIfEmpty(Mono.error(NOT_FOUND))
		// Save history
		.flatMap(old -> {
		    var history = historyMapper.toHistory(old);
		    history.setId(null);
		    history.setStatus("RESCHEDULED");

		    return historyRepository.save(history)
			    .then(Mono.just(old));
		})
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
    public Flux<AppointmentHistory> getHistory(String publicId) {

	return historyRepository.findRevisions(publicId)
		.switchIfEmpty(Flux.error(new NotFoundException("No history found for that ID")));
    }

    /**
     * Generate a calendar file (ICS) for the given appointment ID if exists.
     * 
     * @param publicId Appointment ID (public)
     * @return Calendar file as a {@link ByteArrayResource}
     */
    public Mono<Resource> genCalendarFile(String publicId) {
	return repository.findByPublicId(publicId)
		.switchIfEmpty(Mono.error(NOT_FOUND))
		.flatMap(a -> periodRepository.findById(a.getPeriodId())
			.zipWith(Mono.just(a)))
		.flatMap(tup2 -> {
		    VEvent event = new VEvent(tup2.getT1().getStart(),
			    tup2.getT1().getEnd(),
			    tup2.getT2().getDescription());

		    Calendar calendar = new Calendar();
		    calendar.add(new ProdId("-AppointmentMS//EN"))
			    .add(new Version(new ParameterList(), Version.VALUE_2_0))
			    .add(new CalScale(CalScale.VALUE_GREGORIAN))
			    .add(new Uid(tup2.getT2().getPublicId()));
		    calendar.add(event);

		    byte[] calendarByte = calendar.toString().getBytes();

		    if (calendarByte.length == 0) {
			return Mono.error(new CalendarGenerationException("Failed to generate calendar file"));
		    }

		    return Mono.just(new ByteArrayResource(calendarByte));
		});
    }

    /**
     * Save without incrementing the version. No history is created.
     * 
     * @param appointment
     * @return
     */
    private Mono<Appointment> save(Appointment appointment, boolean update) {
	return Mono.just(appointment)
		// Set service provider ID if exists else throw an error
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
			    .switchIfEmpty(Mono.error(new NotFoundException("Service provider not found!")))
			    .map(s -> {
				a.setServiceProviderId(s.getId());

				return a;
			    });
		})
		// Save period if exists else throw an error
		.flatMap(a -> {
		    if (a.getPeriod() == null) {
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
			    });
		})
		.flatMap(a -> repository.save(a))
		.onErrorComplete(e -> {
		    log.error("Failed to save appointment: " + e.getLocalizedMessage());

		    return false;
		})
		// Save participants if exists else throw an error
		.flatMap(a -> {
		    // If its update, return; Use specific end point to update participants
		    if (update) {
			return Mono.just(a);
		    } else if (a.getParticipants() == null) {
			return Mono.error(new AppointmentException("No participants are defined"));
		    }

		    return Mono.just(a.getParticipants())
			    .flatMapMany(Flux::fromIterable)
			    .map(p -> {
				p.setAppointmentId(a.getId());
				p.setAppointment(a);

				return p;
			    })
			    // Create participant
			    .flatMap(p -> participantService.create(p))
			    // Merge all flux operations
			    .collectList()
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

    protected Mono<Appointment> findByPublicIdEager(String publicId) {
	return repository.findByPublicId(publicId)
		.switchIfEmpty(Mono.error(NOT_FOUND))
		// Set service provider and return
		.flatMap(a -> serviceProviderService.findByIdEager(a.getServiceProviderId())
			.map(p -> {
			    a.setServiceProvider(p);

			    return a;
			}))
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
		.flatMap(a -> participantRepository.findByAppointmentId(a.getId())
			.flatMap(p -> participantService.findByIdEager(p.getId()))
			.collectList()
			.map(parts -> {
			    a.setParticipants(parts);

			    return a;
			}));
    }

    public Mono<String> getFHIRAppointment(String publicId) {
	return findByPublicIdEager(publicId)
		.switchIfEmpty(Mono.error(NOT_FOUND))
		.map(mapper::toModel)
		.map(fhirMapper::toFHIR)
		.map(a -> {
		    IParser parser = FhirContext.forR4().newJsonParser();

		    return parser.encodeResourceToString(a);
		});
    }
}
