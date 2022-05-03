package ms.asp.appointment.repositories;

import ms.asp.appointment.domain.Appointement;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointementRepository extends BaseRepository<Appointement, Long> {

}
