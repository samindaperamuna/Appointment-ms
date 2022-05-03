package ms.asp.appointment.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name = "PARTICIPANT")
@Entity
@Audited
public class Participant extends BaseEntity{

  public String type;

  @OneToOne
  public Period period;

  @OneToOne
  public ParticipantInfo participantInfo;
  public boolean required;
  public String status;

  @Override
  public String toString(){
    return Objects.toString(participantInfo);
  }
}
