package ms.asp.appointment.domain;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Schedule extends BaseEntity {
  private boolean active;

  @OneToMany
  private Set<ParticipantInfo> participantInfo;
  @OneToOne
  private Period planningHorizon;
  private String comment;
}
