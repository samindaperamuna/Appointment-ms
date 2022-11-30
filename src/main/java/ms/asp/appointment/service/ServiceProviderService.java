package ms.asp.appointment.service;

import static ms.asp.appointment.util.CommonUtils.generatePublicId;

import java.util.HashSet;
import java.util.Set;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ms.asp.appointment.domain.ServiceProvider;
import ms.asp.appointment.domain.Slot;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.mapper.ServiceProviderMapper;
import ms.asp.appointment.model.ServiceProviderModel;
import ms.asp.appointment.repository.AvailabilityRepository;
import ms.asp.appointment.repository.BaseRepository;
import ms.asp.appointment.repository.ContactRepository;
import ms.asp.appointment.repository.ServiceProviderAvailabilityRepository;
import ms.asp.appointment.repository.ServiceProviderRepository;
import ms.asp.appointment.repository.ServiceProviderSlotRepository;
import ms.asp.appointment.repository.SlotRepository;
import ms.asp.appointment.util.JSONUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ServiceProviderService extends AbstractService<ServiceProvider, Long, ServiceProviderModel> {

    private final ContactRepository contactRepository;
    private final SlotRepository slotRepository;
    private final ServiceProviderSlotRepository providerSlotRepository;
    private final AvailabilityRepository availabilityRepository;
    private final ServiceProviderAvailabilityRepository providerAvailabilityRepository;

    /**
     * Pass in the repository and the mapper to the super class.
     * 
     * @param repository Implementation of {@link BaseRepository} to pass to the super class
     * @param mapper     Implementation of {@link Mapper} to pass to the super class/
     */
    public ServiceProviderService(
	    ServiceProviderRepository repository,
	    ContactRepository contactRepository,
	    SlotRepository slotRepository,
	    ServiceProviderSlotRepository providerSlotRepository,
	    AvailabilityRepository availabilityRepository,
	    ServiceProviderAvailabilityRepository providerAvailabilityRepository,
	    ServiceProviderMapper mapper) {

	super(repository, mapper);

	this.contactRepository = contactRepository;
	this.slotRepository = slotRepository;
	this.providerSlotRepository = providerSlotRepository;
	this.availabilityRepository = availabilityRepository;
	this.providerAvailabilityRepository = providerAvailabilityRepository;
    }

    public Mono<ServiceProviderModel> create(ServiceProviderModel model) {
	var serviceProvider = mapper.toEntity(model);
	serviceProvider.setPublicId(generatePublicId());

	return save(serviceProvider);
    }

    public Mono<ServiceProviderModel> update(ServiceProviderModel model) {
	return save(mapper.toEntity(model));
    }

    public Mono<ServiceProviderModel> delete(String publicId) {
	var e = new NotFoundException("No ServiceProvider has id = " + publicId);

	repository.findByPublicId(publicId)
		.switchIfEmpty(Mono.error(e))
		.flatMap(p -> contactRepository.deleteById(p.getContactId())
			.onErrorStop()
			.then(Mono.just(p)))
		.flatMap(p -> {
		    Set<Slot> slots = new HashSet<>(p.getAmSlots());
		    slots.addAll(p.getPmSlots());

		    Mono.just(slots)
			    .flatMapMany(Flux::fromIterable)
			    .flatMap(s -> {
				providerSlotRepository.deleteServiceProviderSlot(p, s);

				return Mono.just(s);
			    })
			    .flatMap(s -> slotRepository.delete(s));

		    return Mono.just(p);
		}).flatMap(p -> {
		    Mono.just(p.getAvailabilty())
			    .flatMapMany(Flux::fromIterable)
			    .flatMap(a -> {
				providerAvailabilityRepository.deleteServiceProviderAvailability(p, a);

				return Mono.just(a);
			    }).flatMap(a -> availabilityRepository.delete(a));

		    return Mono.just(p);
		}).flatMap(p -> {
		    repository.delete(p);

		    return Mono.just(p);
		}).map(mapper::toModel);

	return null;
    }

    /**
     * Save without incrementing the version. No history is created.
     * 
     * @param serviceProvider
     * @return
     */
    private Mono<ServiceProviderModel> save(ServiceProvider serviceProvider) {
	Set<Slot> slots = new HashSet<>(serviceProvider.getAmSlots());
	slots.addAll(serviceProvider.getPmSlots());

	return contactRepository.save(serviceProvider.getContact())
		.map(contact -> {
		    serviceProvider.setContactId(contact.getId());

		    // Map JSON fields
		    serviceProvider.setOffDaysJSON(JSONUtils.objectToJSON(serviceProvider.getOffDays()));
		    serviceProvider.setServiceTypesJSON(JSONUtils.objectToJSON(serviceProvider.getServiceTypes()));

		    return serviceProvider;
		}).flatMap(p -> repository.save(p))
		.flatMap(p -> {
		    Mono.just(slots)
			    .flatMapMany(Flux::fromIterable)
			    .flatMap(slot -> slotRepository.save(slot))
			    .flatMap(slot -> providerSlotRepository.saveServiceProviderSlot(p, slot));

		    return Mono.just(p);
		}).flatMap(p -> {
		    Mono.just(p.getAvailabilty())
			    .flatMapMany(Flux::fromIterable)
			    .flatMap(a -> availabilityRepository.save(a))
			    .flatMap(a -> providerAvailabilityRepository.saveServiceProviderAvailability(p, a));

		    return Mono.just(p);
		}).map(mapper::toModel);
    }
}
