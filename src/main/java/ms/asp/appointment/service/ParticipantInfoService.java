package ms.asp.appointment.service;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ms.asp.appointment.domain.Contact;
import ms.asp.appointment.domain.ParticipantInfo;
import ms.asp.appointment.exception.NotFoundException;
import ms.asp.appointment.mapper.ParticipantInfoMapper;
import ms.asp.appointment.model.participantinfo.ParticipantInfoModel;
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
		.flatMap(pi -> repository.findByPublicId(pi.getPublicId())
			.switchIfEmpty(Mono.error(NOT_FOUND))
			.map(r -> {
			    pi.setId(r.getId());

			    return pi;
			}))
		// Save the participant info
		.flatMap(pi -> save(pi, true))
		// Fetch all including attached entities
		.flatMap(pi -> findByPublicIdEager(pi.getPublicId()));
    }

    public Mono<ParticipantInfo> delete(Long id) {
	return repository.findById(id)
		.switchIfEmpty(Mono.error(NOT_FOUND))
		.flatMap(pi -> {
		    if (pi.getContactId() == null) {
			return Mono.just(pi);
		    }

		    return contactService.delete(pi.getContactId())
			    .then(Mono.just(pi));
		})
		.flatMap(pi -> repository.delete(pi).then(Mono.just(pi)));
    }

    private Mono<ParticipantInfo> save(ParticipantInfo participantInfo, boolean update) {
	return Mono.just(participantInfo)
		.flatMap(pi -> {
		    if (pi.getContact() == null) {
			return Mono.just(pi);
		    }

		    return Mono.just(pi.getContact())
			    .flatMap(c -> switchUpdate(pi, c, update));
		})
		.flatMap(repository::save);
    }

    protected Mono<ParticipantInfo> findByPublicIdEager(String publicId) {
	return repository.findByPublicId(publicId)
		.switchIfEmpty(Mono.error(NOT_FOUND))
		.flatMap(pi -> {
		    if (pi.getContactId() == null) {
			return Mono.just(pi);
		    }

		    return contactRepository.findById(pi.getContactId())
			    .map(c -> {
				pi.setContact(c);

				return pi;
			    });
		});
    }

    private Mono<ParticipantInfo> switchUpdate(ParticipantInfo pi, Contact contact, boolean update) {
	if (update) {
	    return contactService.update(contact)
		    .map(c -> {
			pi.setContactId(c.getId());

			return pi;
		    });
	} else {
	    return contactService.create(contact)
		    .map(c -> {
			pi.setContactId(c.getId());

			return pi;
		    });
	}
    }
}
