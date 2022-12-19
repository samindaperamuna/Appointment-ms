package ms.asp.appointment.service;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ms.asp.appointment.domain.Availability;
import ms.asp.appointment.exception.AvailabilityException;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.mapper.AvailabilityMapper;
import ms.asp.appointment.model.AvailabilityModel;
import ms.asp.appointment.repository.AvailabilityRepository;
import ms.asp.appointment.repository.BaseRepository;
import ms.asp.appointment.repository.SlotRepository;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AvailabilityService extends AbstractService<Availability, Long, AvailabilityModel> {

    private final SlotRepository slotRepository;

    /**
     * Pass in the repository and the mapper to the super class.
     * 
     * @param repository Implementation of {@link BaseRepository} to pass to the super class
     * @param mapper     Implementation of {@link Mapper} to pass to the super class/
     */
    public AvailabilityService(
	    AvailabilityRepository repository,
	    SlotRepository slotRepository,
	    AvailabilityMapper mapper) {

	super(repository, mapper);

	this.slotRepository = slotRepository;
    }

    public Mono<AvailabilityModel> findOne(String publicId) {
	return findByPublicIdEager(publicId)
		.map(mapper::toModel);
    }

    public Mono<AvailabilityModel> create(AvailabilityModel model) {
	return Mono.just(model)
		.map(mapper::toEntity)
		.flatMap(this::create)
		.map(mapper::toModel);
    }

    protected Mono<Availability> create(Availability availability) {
	return save(availability, false);
    }

    public Mono<AvailabilityModel> update(AvailabilityModel model) {
	return Mono.just(model)
		.map(mapper::toEntity)
		.flatMap(this::update)
		.map(mapper::toModel);
    }

    public Mono<Availability> update(Availability availability) {
	return Mono.just(availability)
		// Get saved availability
		.flatMap(a -> repository.findByPublicId(a.getPublicId())
			.switchIfEmpty(Mono.error(new NotFoundException("No availability found for that ID")))
			.map(r -> {
			    a.setId(r.getId());

			    return a;
			}))
		// Save the slot
		.flatMap(a -> save(a, true))
		// Fetch all including attached entities
		.flatMap(p -> findByPublicIdEager(p.getPublicId()));
    }

    public Mono<AvailabilityModel> delete(String publicId) {
	return findByPublicIdEager(publicId)
		.switchIfEmpty(Mono.error(new NotFoundException("No availability found for that ID")))
		.flatMap(a -> repository.delete(a).then(Mono.just(a)))
		.map(mapper::toModel);
    }

    private Mono<Availability> save(Availability availability, boolean update) {
	return Mono.just(availability)
		.flatMap(a -> {
		    // Check if ID is already set
		    if (a.getSlotId() != null) {
			return Mono.just(a);
		    }

		    return slotRepository.findByPublicId(a.getSlot().getPublicId())
			    .switchIfEmpty(Mono.error(new AvailabilityException("Slot not found")))
			    .map(s -> {
				a.setSlotId(s.getId());

				return a;
			    });
		})
		.flatMap(repository::save);
    }

    private Mono<Availability> findByPublicIdEager(String publicId) {
	return repository.findByPublicId(publicId)
		.switchIfEmpty(Mono.error(new NotFoundException("No availability found for that ID")))
		.flatMap(a -> slotRepository.findById(a.getSlotId())
			.map(s -> {
			    a.setSlot(s);

			    return a;
			}));
    }
}
