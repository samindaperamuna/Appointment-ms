package ms.asp.appointment.services;

import static ms.asp.appointment.utils.CommonUtils.generatePublicId;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Function;
import javax.validation.Valid;
import ms.asp.appointment.domain.Appointement;
import ms.asp.appointment.domain.Period;
import ms.asp.appointment.exceptions.NotFoundException;
import ms.asp.appointment.mappers.AppointementResponseMapper;
import ms.asp.appointment.models.AppointementResponse;
import ms.asp.appointment.models.AppointmentsHistoryResponse;
import ms.asp.appointment.models.PeriodModel;
import ms.asp.appointment.repositories.BaseRepository;
import org.springframework.data.history.Revision;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class AppointementService extends AbstractService<Appointement, Long, AppointementResponse> {

  public AppointementService(BaseRepository<Appointement, Long> repository,
      AppointementResponseMapper mapper) {
    super(repository, mapper);
  }

  public Flux<AppointmentsHistoryResponse> history(String publicId){
    var entityId = repository.findByPublicId(publicId).map(Appointement::getId).orElseThrow(()-> new NotFoundException("no Appointement has id = "+ publicId));
    return Flux.fromIterable(repository.findRevisions(entityId).map(rev -> ((AppointementResponseMapper) mapper).toHistory(rev.getEntity())));
  }

  public Mono<AppointementResponse> create(AppointementResponse request) {
    var appointment = mapper.toEntity(request);
    appointment.setPublicId(generatePublicId());
    return save(appointment);
  }

  public Mono<AppointementResponse> update(AppointementResponse request) {
    var appointment = mapper.toEntity(request);
    return save(appointment);
  }

  private Mono<AppointementResponse> save(Appointement request) {
    return  Mono.just(mapper.toModel(repository.save(request)));
  }



  public Mono<AppointementResponse> reschedule(String appointementPublicId, @Valid PeriodModel rescheduleRequest) {
    var appoint = repository.findByPublicId(appointementPublicId).orElseThrow(()-> new NotFoundException("no Appointement has id = "+ appointementPublicId));

    appoint.setStart(rescheduleRequest.getStart());
    appoint.setEnd(rescheduleRequest.getEnd());

    return  Mono.just(mapper.toModel(repository.save(appoint)));
  }
}
