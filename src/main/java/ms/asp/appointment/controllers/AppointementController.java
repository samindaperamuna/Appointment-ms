package ms.asp.appointment.controllers;

import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ms.asp.appointment.domain.Appointement;
import ms.asp.appointment.domain.Period;
import ms.asp.appointment.models.AppointementResponse;
import ms.asp.appointment.models.AppointmentsHistoryResponse;
import ms.asp.appointment.models.PeriodModel;
import ms.asp.appointment.services.AppointementService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping(("/appointement"))
@Slf4j
public class AppointementController {
  private final AppointementService appointementService;

  @GetMapping
  Flux<AppointementResponse> getAll(Pageable pageable){
    return appointementService.findAll(pageable);
  }

  @GetMapping("/{publicId}/history")
  Flux<AppointmentsHistoryResponse> history(@PathVariable String publicId){
    return appointementService.history(publicId);
  }

  @GetMapping("/{publicId}")
  Optional<Appointement> findById(@PathVariable String publicId){
    return appointementService.findOne(publicId);
  }

  @DeleteMapping("/{publicId}")
  Mono<AppointementResponse> delete(@PathVariable String publicId){
    return appointementService.delete(publicId);
  }

  @PostMapping
  Mono<AppointementResponse> create(@Valid @RequestBody AppointementResponse request){
    return appointementService.create(request);
  }

  @PutMapping
  Mono<AppointementResponse> update(@Valid @RequestBody AppointementResponse request){
    return appointementService.update(request);
  }

  @PutMapping("/{publicId}/reschedule")
  Mono<AppointementResponse> reschedule(@PathVariable String publicId, @Valid @RequestBody PeriodModel rescheduleRequest){
    return appointementService.reschedule(publicId, rescheduleRequest);
  }
}
