package ms.asp.appointment.service;

import java.time.LocalTime;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ms.asp.appointment.domain.Availability;
import ms.asp.appointment.domain.Slot;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.exception.SlotException;
import ms.asp.appointment.mapper.AvailabilityMapper;
import ms.asp.appointment.mapper.SlotMapper;
import ms.asp.appointment.model.SlotModel;
import ms.asp.appointment.repository.AvailabilityRepository;
import ms.asp.appointment.repository.BaseRepository;
import ms.asp.appointment.repository.ServiceProviderRepository;
import ms.asp.appointment.repository.SlotRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class SlotService extends AbstractService<Slot, Long, SlotModel> {

    private final AvailabilityRepository availabilityRepository;
    private final ServiceProviderRepository providerRepository;
    private final AvailabilityService availabilityService;
    private final AvailabilityMapper availabilityMapper;

    /**
     * Pass in the repository and the mapper to the super class.
     * 
     * @param repository Implementation of {@link BaseRepository} to pass to the super class
     * @param mapper     Implementation of {@link Mapper} to pass to the super class/
     */
    public SlotService(
	    SlotRepository repository,
	    AvailabilityRepository availabilityRepository,
	    ServiceProviderRepository providerRepository,
	    AvailabilityService availabilityService,
	    SlotMapper mapper,
	    AvailabilityMapper availabilityMapper) {

	super(repository, mapper);

	this.availabilityRepository = availabilityRepository;
	this.providerRepository = providerRepository;
	this.availabilityService = availabilityService;
	this.availabilityMapper = availabilityMapper;
    }

    public Mono<SlotModel> findOne(String publicId) {
	return findByPublicIdEager(publicId)
		.map(mapper::toModel);
    }

    public Flux<SlotModel> findAMSlots(Long serviceProviderId) {
	return ((SlotRepository) repository).findByProviderAndStartBetween(serviceProviderId,
		LocalTime.MIDNIGHT, LocalTime.NOON)
		.flatMap(s -> findByPublicIdEager(s.getPublicId()))
		.map(mapper::toModel);

    }

    public Flux<SlotModel> findPMSlots(Long serviceProviderId) {
	return ((SlotRepository) repository).findByProviderAndStartBetween(serviceProviderId,
		LocalTime.NOON, LocalTime.MIDNIGHT)
		.flatMap(s -> findByPublicIdEager(s.getPublicId()))
		.map(mapper::toModel);
    }

    public Mono<SlotModel> create(SlotModel model) {
	return Mono.just(model)
		.flatMap(s -> {
		    if (s.getServiceProvider() == null) {
			return Mono.error(new SlotException("No service provider defined"));
		    }

		    return Mono.just(s);
		})
		.map(mapper::toEntity)
		.flatMap(this::create)
		.map(mapper::toModel);
    }

    protected Mono<Slot> create(Slot slot) {
	return Mono.just(slot)
		.flatMap(s -> save(s, false));
    }

    public Mono<SlotModel> update(SlotModel model) {
	return Mono.just(model)
		.flatMap(s -> {
		    if (s.getServiceProvider() == null) {
			return Mono.error(new SlotException("No service provider defined"));
		    }

		    return Mono.just(s);
		})
		.map(mapper::toEntity)
		.flatMap(this::update)
		.map(mapper::toModel);
    }

    protected Mono<Slot> update(Slot slot) {
	return Mono.just(slot)
		// Get saved slot
		.flatMap(s -> repository.findByPublicId(s.getPublicId())
			.switchIfEmpty(Mono.error(new NotFoundException("No slot found for that ID")))
			.map(r -> {
			    s.setId(r.getId());

			    return s;
			}))
		// Save the slot
		.flatMap(s -> save(s, true))
		// Fetch all including attached entities
		.flatMap(s -> findByPublicIdEager(s.getPublicId()));
    }

    public Mono<SlotModel> delete(String publicId) {
	return findByPublicIdEager(publicId)
		.switchIfEmpty(Mono.error(new NotFoundException("No slot found for that ID")))
		.flatMap(s -> Mono.just(s.getAvailability())
			.flatMapMany(Flux::fromIterable)
			.flatMap(a -> availabilityRepository.delete(a))
			.collectList()
			.then(Mono.just(s)))
		.flatMap(s -> repository.delete(s)
			.then(Mono.just(s)))
		.map(mapper::toModel);
    }

    private Mono<Slot> save(Slot slot, boolean update) {
	return Mono.just(slot)
		.flatMap(s -> {
		    // Check if ID is already set
		    if (s.getServiceProviderId() != null) {
			return Mono.just(s);
		    }

		    return providerRepository.findByPublicId(s.getServiceProvider().getPublicId())
			    .switchIfEmpty(Mono.error(new SlotException("Service provider not found")))
			    .map(p -> {
				s.setServiceProviderId(p.getId());

				return s;
			    });
		})
		.flatMap(repository::save)
		// Save availability
		.flatMap(s -> {
		    if (s.getAvailability() == null) {
			return Mono.just(s);
		    }

		    if (update) {
			return Mono.just(s.getAvailability())
				.flatMapMany(Flux::fromIterable)
				.map(a -> {
				    a.setSlotId(s.getId());

				    return a;
				})
				.map(availabilityMapper::toModel)
				.flatMap(a -> availabilityService.update(a))
				.collectList()
				.then(Mono.just(s));
		    }

		    return Mono.just(s.getAvailability())
			    .flatMapMany(Flux::fromIterable)
			    .map(a -> {
				a.setSlotId(s.getId());

				return a;
			    })
			    .flatMap(a -> switchUpdate(a, update))
			    .collectList()
			    .then(Mono.just(s));
		});
    }

    private Mono<Slot> findByPublicIdEager(String publicId) {
	return repository.findByPublicId(publicId)
		.switchIfEmpty(Mono.error(new NotFoundException("No slot found for that ID")))
		.flatMap(s -> providerRepository.findById(s.getServiceProviderId())
			.map(p -> {
			    s.setServiceProvider(p);

			    return s;
			}))
		// Parse and set service types
		.flatMap(s -> availabilityRepository.findBySlotId(s.getId())
			.collectList()
			.map(a -> {
			    s.setAvailability(a);

			    return s;
			}));
    }

    private Mono<Availability> switchUpdate(Availability availability, boolean update) {
	if (update) {
	    return availabilityService.update(availability);
	} else {
	    return availabilityService.create(availability);
	}
    }
}