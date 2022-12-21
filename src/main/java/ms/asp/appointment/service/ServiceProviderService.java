package ms.asp.appointment.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ms.asp.appointment.domain.AvailabilityType;
import ms.asp.appointment.domain.ServiceProvider;
import ms.asp.appointment.domain.Slot;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.exception.ServiceProviderException;
import ms.asp.appointment.mapper.AppointmentMapper;
import ms.asp.appointment.mapper.ServiceProviderMapper;
import ms.asp.appointment.mapper.SlotMapper;
import ms.asp.appointment.model.Schedule;
import ms.asp.appointment.model.ServiceProviderModel;
import ms.asp.appointment.repository.AppointmentRepository;
import ms.asp.appointment.repository.BaseRepository;
import ms.asp.appointment.repository.ContactRepository;
import ms.asp.appointment.repository.ServiceProviderRepository;
import ms.asp.appointment.util.JSONUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ServiceProviderService extends AbstractService<ServiceProvider, Long, ServiceProviderModel> {

    private final AppointmentRepository appointmentRepository;
    private final ContactRepository contactRepository;
    private final SlotMapper slotMapper;
    private final AppointmentMapper appointmentMapper;
    private final SlotService slotService;

    private static final NotFoundException NOT_FOUND = new NotFoundException("No service provider found for that ID");

    /**
     * Pass in the repository and the mapper to the super class.
     * 
     * @param repository Implementation of {@link BaseRepository} to pass to the super class
     * @param mapper     Implementation of {@link Mapper} to pass to the super class/
     */
    public ServiceProviderService(
	    ServiceProviderRepository repository,
	    AppointmentRepository appointmentRepository,
	    ContactRepository contactRepository,
	    ServiceProviderMapper mapper,
	    SlotMapper slotMapper,
	    AppointmentMapper appointmentMapper,
	    SlotService slotService) {

	super(repository, mapper);

	this.appointmentRepository = appointmentRepository;
	this.contactRepository = contactRepository;
	this.slotMapper = slotMapper;
	this.appointmentMapper = appointmentMapper;
	this.slotService = slotService;
    }

    public Flux<ServiceProviderModel> findByAvailability(AvailabilityType availabilityType) {
	return ((ServiceProviderRepository) repository).findBy(availabilityType)
		.flatMap(p -> {
		    return findByPublicIdEager(p.getPublicId());
		})
		.map(mapper::toModel);
    }

    public Mono<ServiceProvider> create(ServiceProvider serviceProvider) {
	return save(serviceProvider, false)
		// Fetch all including attached entities
		.flatMap(p -> findByPublicIdEager(p.getPublicId()));
    }

    public Mono<ServiceProvider> update(ServiceProvider serviceProvider) {
	return Mono.just(serviceProvider)
		// Get saved service provider
		.flatMap(p -> repository.findByPublicId(p.getPublicId())
			.switchIfEmpty(Mono.error(NOT_FOUND))
			.map(r -> {
			    p.setId(r.getId());
			    p.setCreated(r.getCreated());
			    p.setModified(r.getModified());
			    p.setVersion(r.getVersion());

			    return p;
			}))
		// Get saved contact
		.flatMap(p -> {
		    if (p.getContact() == null)
			return Mono.just(p);

		    return contactRepository.findByPublicId(p.getContact().getPublicId())
			    .switchIfEmpty(Mono.error(new NotFoundException("No contact found for that ID")))
			    .map(c -> {
				p.getContact().setId(c.getId());

				return p;
			    });
		})
		// Save the service provider
		.flatMap(p -> save(p, true))
		// Fetch all including attached entities
		.flatMap(p -> findByPublicIdEager(p.getPublicId()));
    }

    public Mono<ServiceProvider> delete(Long id) {
	var e = new ServiceProviderException("Service provider has linked appointments");

	return repository.findById(id)
		.switchIfEmpty(Mono.error(NOT_FOUND))
		// Find appointments
		.flatMap(p -> {
		    return appointmentRepository.findByServiceProviderId(p.getId())
			    .hasElements()
			    .flatMap(hasElements -> {
				if (hasElements)
				    return Mono.error(e);
				else
				    return Mono.just(p);
			    });
		})
		// Delete contact
		.flatMap(p -> contactRepository.deleteById(p.getContactId())
			.onErrorStop()
			.then(Mono.just(p)))
		// Delete the slots
		.flatMap(p -> {
		    Set<Slot> slots = new HashSet<>(p.getAmSlots());
		    slots.addAll(p.getPmSlots());

		    return Mono.just(slots)
			    .flatMapMany(Flux::fromIterable)
			    .flatMap(s -> slotService.delete(s.getPublicId()))
			    .then(Mono.just(p));
		})
		.flatMap(p -> {
		    return repository.delete(p)
			    .then(Mono.just(p));
		});
    }

    public Mono<Schedule> findSchedule(String publicId, LocalDateTime begin, LocalDateTime end) {
	return repository.findByPublicId(publicId)
		.switchIfEmpty(Mono.error(NOT_FOUND))
		.flatMap(p -> slotService.findAMSlots(p.getId())
			.concatWith(slotService.findPMSlots(p.getId()))
			.collectList()
			.zipWith(Mono.just(p)))
		.flatMap(tuple -> appointmentRepository
			.findByBetween(tuple.getT2().getId(), begin, end)
			.map(appointmentMapper::toModel)
			.collectList()
			.map(a -> {
			    Schedule s = new Schedule();
			    s.setAppointments(a);
			    s.setSlots(tuple.getT1());

			    return s;
			}));
    }

    /**
     * This method handles both create and update calls.
     * 
     * @param serviceProvider
     * @return
     */
    private Mono<ServiceProvider> save(ServiceProvider serviceProvider, boolean update) {
	Set<Slot> slots = new HashSet<>();

	if (serviceProvider.getAmSlots() == null || serviceProvider.getAmSlots().isEmpty()
		|| serviceProvider.getPmSlots() == null || serviceProvider.getPmSlots().isEmpty()) {

	    if (!update) {
		return Mono.error(new ServiceProviderException("Missing slot information"));
	    }
	} else {
	    slots.addAll(serviceProvider.getAmSlots());
	    slots.addAll(serviceProvider.getPmSlots());
	}

	return Mono.just(serviceProvider)
		// Save contact and return
		.flatMap(p -> {
		    if (p.getContact() == null) {
			// If its update and no contact, return
			if (update) {
			    return Mono.just(p);
			} else {
			    return Mono.error(new ServiceProviderException("Missing contact details"));
			}
		    }

		    return contactRepository.save(p.getContact());
		})
		.map(c -> {
		    serviceProvider.setContactId(c.getId());

		    return serviceProvider;
		})
		// Parse and set off days
		.flatMap(p -> {
		    if (p.getOffDays() == null) {
			// If its update and no off days, return
			if (update) {
			    return Mono.just(p);
			} else {
			    return Mono.error(new ServiceProviderException("Off days not provided"));

			}
		    }

		    serviceProvider.setOffDaysJSON(JSONUtils.objectToJSON(serviceProvider.getOffDays()));

		    return Mono.just(p);
		})
		// Parse and set service types
		.flatMap(p -> {
		    if (p.getServiceTypes() == null) {
			// If its update and no service types, return
			if (update) {
			    return Mono.just(p);
			} else {
			    return Mono.error(new ServiceProviderException("Service types not defined"));
			}
		    }

		    serviceProvider.setServiceTypesJSON(JSONUtils.serviceTypeToJSON(serviceProvider.getServiceTypes()));

		    return Mono.just(p);
		})
		// Save service provider
		.flatMap(p -> repository.save(p))
		// Save slots and return
		.flatMap(p -> {
		    // Use slots end-point to update slots
		    if (update) {
			return Mono.just(p);
		    }

		    if (slots.isEmpty()) {
			return Mono.error(new ServiceProviderException("No slots are defined"));
		    }

		    return Mono.just(slots)
			    .flatMapMany(Flux::fromIterable)
			    .map(s -> {
				s.setServiceProviderId(p.getId());
				s.setValidDaysJSON(JSONUtils.objectToJSON(s.getValidDays()));

				return s;
			    })
			    .flatMap(s -> slotService.create(s))
			    .collectList()
			    .then(Mono.just(p));

		});
    }

    /**
     * Fetch a {@link ServiceProvider} by its public ID, all associated fields populated.
     * 
     * @param publicId String
     * @return {@link Mono<ServiceProvider>}
     */
    protected Mono<ServiceProvider> findByPublicIdEager(String publicId) {
	return repository.findByPublicId(publicId)
		.switchIfEmpty(Mono.error(NOT_FOUND))
		// Parse and set off days
		.map(p -> {
		    if (p.getOffDaysJSON() != null || !p.getOffDaysJSON().isBlank())
			p.setOffDays(JSONUtils.jsonToOffDays(p.getOffDaysJSON()));

		    return p;
		})
		// Parse and set service types
		.map(p -> {
		    if (p.getServiceTypesJSON() != null || !p.getServiceTypesJSON().isBlank())
			p.setServiceTypes(JSONUtils.jsonToServiceType(p.getServiceTypesJSON()));

		    return p;
		})
		// Set contact and return
		.flatMap(p -> {
		    return contactRepository.findById(p.getContactId())
			    .map(c -> {
				p.setContact(c);

				return p;
			    });
		})
		// Set AM slots and return
		.flatMap(p -> slotService.findAMSlots(p.getId())
			.map(slotMapper::toEntity)
			.collectList()
			.map(s -> {
			    p.setAmSlots(s);
			    return p;
			}))
		// Set PM slots and return
		.flatMap(p -> slotService.findAMSlots(p.getId())
			.map(slotMapper::toEntity)
			.collectList()
			.map(s -> {
			    p.setPmSlots(s);
			    return p;
			}));
    }
}
