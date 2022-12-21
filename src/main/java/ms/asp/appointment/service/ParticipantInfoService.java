package ms.asp.appointment.service;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ms.asp.appointment.domain.Contact;
import ms.asp.appointment.domain.ParticipantInfo;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.mapper.ParticipantInfoMapper;
import ms.asp.appointment.model.ParticipantInfoModel;
import ms.asp.appointment.repository.BaseRepository;
import ms.asp.appointment.repository.ContactRepository;
import ms.asp.appointment.repository.ParticipantInfoRepository;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class ParticipantInfoService extends AbstractService<ParticipantInfo, Long, ParticipantInfoModel> {

    private final ContactRepository contactRepository;
    private final ContactService contactService;

    private static final NotFoundException NOT_FOUND = new NotFoundException("No participant info found for that ID");

    /**
     * Pass in the repository and the mapper to the super class.
     * 
     * @param repository Implementation of {@link BaseRepository} to pass to the super class
     * @param mapper     Implementation of {@link Mapper} to pass to the super class/
     */
    public ParticipantInfoService(
	    ParticipantInfoRepository repository,
	    ContactRepository contactRepository,
	    ContactService contactService,
	    ParticipantInfoMapper mapper) {

	super(repository, mapper);

	this.contactRepository = contactRepository;
	this.contactService = contactService;
    }

    protected Mono<ParticipantInfo> create(ParticipantInfo participantInfo) {
	return save(participantInfo, false);
    }

    public Mono<ParticipantInfo> update(ParticipantInfo participantInfo) {
	return Mono.just(participantInfo)
		// Get saved participant info
		.flatMap(a -> repository.findByPublicId(a.getPublicId())
			.switchIfEmpty(Mono.error(NOT_FOUND))
			.map(r -> {
			    a.setId(r.getId());

			    return a;
			}))
		// Save the participant info
		.flatMap(a -> save(a, true))
		// Fetch all including attached entities
		.flatMap(p -> findByPublicIdEager(p.getPublicId()));
    }

    public Mono<ParticipantInfo> delete(Long id) {
	return repository.findById(id)
		.switchIfEmpty(Mono.error(NOT_FOUND))
		.map(pi -> {
		    contactService.delete(pi.getContactId());

		    return pi;
		})
		.flatMap(a -> repository.delete(a).then(Mono.just(a)));
    }

    private Mono<ParticipantInfo> save(ParticipantInfo participantInfo, boolean update) {
	return Mono.just(participantInfo)
		.flatMap(pi -> {
		    if (pi.getContact() != null) {
			return Mono.just(pi);
		    }

		    return Mono.just(pi.getContact())
			    .flatMap(c -> switchUpdate(c, update))
			    .then(Mono.just(pi));
		})
		.flatMap(repository::save);
    }

    protected Mono<ParticipantInfo> findByPublicIdEager(String publicId) {
	return repository.findByPublicId(publicId)
		.switchIfEmpty(Mono.error(NOT_FOUND))
		.flatMap(pi -> contactRepository.findById(pi.getContactId())
			.map(c -> {
			    pi.setContact(c);

			    return pi;
			}));
    }

    private Mono<Contact> switchUpdate(Contact contact, boolean update) {
	if (update) {
	    return contactService.update(contact);
	} else {
	    return contactService.create(contact);
	}
    }
}
