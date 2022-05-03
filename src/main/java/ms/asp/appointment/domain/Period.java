package ms.asp.appointment.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@Table(name = "PERIOD")
@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Period extends BaseEntity{
  private LocalDateTime start;
  private LocalDateTime end;
}
