package ms.asp.appointment.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ms.asp.appointment.domain.AvailabilityType;
import ms.asp.appointment.domain.ServiceProvider;
import ms.asp.appointment.domain.Slot;
import ms.asp.appointment.exception.AppointmentException;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.exception.ServiceProviderException;
import ms.asp.appointment.mapper.AppointmentMapper;
import ms.asp.appointment.mapper.ServiceProviderMapper;
import ms.asp.appointment.mapper.SlotMapper;
import ms.asp.appointment.model.Schedule;
import ms.asp.appointment.model.ServiceProviderModel;
import ms.asp.appointment.repository.AppointmentRepository;
import ms.asp.appointment.repository.AvailabilityRepository;
import ms.asp.appointment.repository.BaseRepository;
import ms.asp.appointment.repository.ContactRepository;
import ms.asp.appointment.repository.ServiceProviderRepository;
import ms.asp.appointment.repository.ServiceProviderSlotRepository;
import ms.asp.appointment.repository.SlotAvailabilityRepository;
import ms.asp.appointment.repository.SlotRepository;
import ms.asp.appointment.util.JSONUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ServiceProviderService extends AbstractService<ServiceProvider, Long, ServiceProviderModel> {

    private final AppointmentRepository appointmentRepository;
    private final ContactRepository contactRepository;
    private final SlotRepository slotRepository;
    private final ServiceProviderSlotRepository providerSlotRepository;
    private final AvailabilityRepository availabilityRepository;
    private final SlotAvailabilityRepository slotAvailabilityRepository;
    private final SlotMapper slotMapper;
    private final AppointmentMapper appointmentMapper;

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
	    SlotRepository slotRepository,
	    ServiceProviderSlotRepository providerSlotRepository,
	    AvailabilityRepository availabilityRepository,
	    SlotAvailabilityRepository providerAvailabilityRepository,
	    ServiceProviderMapper mapper,
	    SlotMapper slotMapper,
	    AppointmentMapper appointmentMapper) {

	super(repository, mapper);

	this.appointmentRepository = appointmentRepository;
	this.contactRepository = contactRepository;
	this.slotRepository = slotRepository;
	this.providerSlotRepository = providerSlotRepository;
	this.availabilityRepository = availabilityRepository;
	this.slotAvailabilityRepository = providerAvailabilityRepository;
	this.slotMapper = slotMapper;
	this.appointmentMapper = appointmentMapper;
    }

    public Mono<Page<ServiceProviderModel>> findByPage(PageRequest pageRequest) {
	return this.repository.findBy(pageRequest)
		.flatMap(p -> findByPublicIdEager(p.getPublicId()))
		.map(mapper::toModel)
		.collectList()
		.zipWith(this.repository.count())
		.map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    public Flux<ServiceProviderModel> findByAvailability(AvailabilityType availabilityType) {
	return ((ServiceProviderRepository) repository).findBy(availabilityType)
		.flatMap(p -> {
		    return findByPublicIdEager(p.getPublicId());
		})
		.map(mapper::toModel);
    }

    public Mono<ServiceProviderModel> findOne(String publicId) {
	return findByPublicIdEager(publicId)
		.map(mapper::toModel);
    }

    public Mono<ServiceProviderModel> create(ServiceProviderModel model) {
	var serviceProvider = mapper.toEntity(model);

	return save(serviceProvider, false)
		// Fetch all including attached entities
		.flatMap(p -> findByPublicIdEager(p.getPublicId()))
		.map(mapper::toModel);
    }

    public Mono<ServiceProviderModel> update(ServiceProviderModel model) {
	return Mono.just(mapper.toEntity(model))
		// Get saved service provider
		.flatMap(p -> repository.findByPublicId(p.getPublicId())
			.switchIfEmpty(Mono.error(new NotFoundException("No service provider found for that ID")))
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
		// Get saved slots
//		.flatMap(p -> {
//		    if ((p.getAmSlots() == null || p.getAmSlots().isEmpty())
//			    && (p.getPmSlots() == null || p.getPmSlots().isEmpty()))
//			return Mono.just(p);
//
//		    Set<Slot> slots = new HashSet<>(p.getAmSlots());
//		    slots.addAll(p.getPmSlots());
//
//		    return Mono.just(slots)
//			    .flatMapMany(Flux::fromIterable)
//			    .flatMap(s -> slotRepository.findByPublicId(s.getPublicId())
//				    .switchIfEmpty(Mono.error(new NotFoundException("No slot found for that ID")))
//				    .map(slot -> {
//					s.setId(slot.getId());
//
//					return s;
//				    }))
//			    .collect(Collectors.toSet())
//			    .then(Mono.just(p));
//		})
		// Save the service provider
		.flatMap(p -> save(p, true))
		// Fetch all including attached entities
		.flatMap(p -> findByPublicIdEager(p.getPublicId()))
		.map(mapper::toModel);
    }

    public Mono<ServiceProviderModel> delete(String publicId) {
	var e = new ServiceProviderException("Service provider has linked appointments");

	return findByPublicIdEager(publicId)
		.switchIfEmpty(Mono.error(new NotFoundException("No service provider found for that ID")))
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
			    // Delete availability
			    .flatMap(s -> {
				return Mono.just(s.getAvailability())
					.flatMapMany(Flux::fromIterable)
					.flatMap(a -> {
					    return slotAvailabilityRepository.deleteSlotAvailability(s, a)
						    .then(Mono.just(a));
					})
					.flatMap(a -> availabilityRepository.delete(a))
					.then(Mono.just(s));
			    })
			    .flatMap(s -> {
				return providerSlotRepository.deleteServiceProviderSlot(p, s)
					.then(Mono.just(s));
			    })
			    .flatMap(s -> slotRepository.delete(s))
			    .then(Mono.just(p));
		})

		.flatMap(p -> {
		    return repository.delete(p)
			    .then(Mono.just(p));
		})
		.map(mapper::toModel);
    }

    public Mono<Schedule> findSchedule(String publicId, LocalDateTime begin, LocalDateTime end) {
	return repository.findByPublicId(publicId)
		.switchIfEmpty(Mono.error(new NotFoundException("No service provider found for that ID")))
		.flatMap(p -> providerSlotRepository.findAMSlots(p, 10)
			.concatWith(providerSlotRepository.findPMSlots(p, 10))
			.map(slotMapper::toModel)
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
		    // If its update return; Use specific end point to update slots
		    if (update) {
			return Mono.just(p);
		    } else if (slots.isEmpty()) {
			return Mono.error(new AppointmentException("No slots are defined"));
		    }

		    return Mono.just(slots)
			    .flatMapMany(Flux::fromIterable)
			    .map(s -> {
				s.setValidDaysJSON(JSONUtils.objectToJSON(s.getValidDays()));

				return s;
			    })
			    .flatMap(slot -> slotRepository.save(slot))
			    .flatMap(slot -> providerSlotRepository.saveServiceProviderSlot(p, slot)
				    .then(Mono.just(slot)))
			    // Save availability and return
			    .flatMap(s -> {
				if (s.getAvailability() == null)
				    // If no availability info, return
				    return Mono.just(s);

				return Mono.just(s.getAvailability())
					.flatMapMany(Flux::fromIterable)
					.flatMap(a -> availabilityRepository.save(a))
					.flatMap(a -> slotAvailabilityRepository.saveSlotAvailability(s, a))
					.collectList()
					.then(Mono.just(s));
			    })
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
    private Mono<ServiceProvider> findByPublicIdEager(String publicId) {
	return repository.findByPublicId(publicId)
		.switchIfEmpty(Mono.error(new NotFoundException("No service provider found for that ID")))
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
		.flatMap(p -> {
		    return providerSlotRepository.findAMSlots(p, 10)
			    .flatMap(s -> slotAvailabilityRepository.findAvailability(s, 10)
				    .collectList()
				    .map(a -> {
					s.setAvailability(a);

					return s;
				    }))
			    .collectList()
			    .map(slots -> {
				p.setAmSlots(slots);

				return p;
			    });
		})
		// Set PM slots and return
		.flatMap(p -> {
		    return providerSlotRepository.findPMSlots(p, 10)
			    .flatMap(s -> slotAvailabilityRepository.findAvailability(s, 10)
				    .collectList()
				    .map(a -> {
					s.setAvailability(a);

					return s;
				    }))
			    .collectList()
			    .map(s -> {
				p.setPmSlots(s);

				return p;
			    });
		});
    }
}
