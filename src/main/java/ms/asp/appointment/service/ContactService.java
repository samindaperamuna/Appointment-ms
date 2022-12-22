package ms.asp.appointment.service;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ms.asp.appointment.domain.Contact;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.mapper.ContactMapper;
import ms.asp.appointment.model.ContactModel;
import ms.asp.appointment.repository.BaseRepository;
import ms.asp.appointment.repository.ContactRepository;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ContactService extends AbstractService<Contact, Long, ContactModel> {

    /**
     * Pass in the repository and the mapper to the super class.
     * 
     * @param repository Implementation of {@link BaseRepository} to pass to the super class
     * @param mapper     Implementation of {@link Mapper} to pass to the super class/
     */
    public ContactService(
	    ContactRepository repository,
	    ContactMapper mapper) {

	super(repository, mapper);
    }

    protected Mono<Contact> create(Contact contact) {
	return save(contact, false);
    }

    public Mono<Contact> update(Contact contact) {
	return Mono.just(contact)
		// Get saved contact
		.flatMap(a -> repository.findByPublicId(a.getPublicId())
			.switchIfEmpty(Mono.error(new NotFoundException("No contact found for that ID")))
			.map(r -> {
			    a.setId(r.getId());

			    return a;
			}))
		// Save the contact
		.flatMap(a -> save(a, true))
		// Fetch all including attached entities
		.flatMap(p -> findByPublicIdEager(p.getPublicId()));
    }

    public Mono<Contact> delete(Long id) {
	return repository.findById(id)
		.switchIfEmpty(Mono.error(new NotFoundException("No contact found for that ID")))
		.flatMap(c -> repository.delete(c).then(Mono.just(c)));
    }

    private Mono<Contact> save(Contact contact, boolean update) {
	return Mono.just(contact)
		.flatMap(repository::save);
    }

    protected Mono<Contact> findByPublicIdEager(String publicId) {
	return repository.findByPublicId(publicId)
		.switchIfEmpty(Mono.error(new NotFoundException("No contact found for that ID")));
    }
}
