package ms.asp.appointment.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@EqualsAndHashCode(callSuper = true)
@Table
@Entity
@Data
@NoArgsConstructor
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Slot extends BaseEntity{

  @OneToOne
  public Schedule schedule;
  public String status;
  public LocalDateTime start;
  public LocalDateTime end;
  public boolean overbooked;
  public String comment;
}
