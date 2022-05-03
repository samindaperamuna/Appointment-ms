package ms.asp.appointment.domain;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name="PARTICIPANT_INFO")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class ParticipantInfo extends BaseEntity{

  private String name;
  @OneToMany
  private Set<Contact> contacts;

  @Override
  public String toString(){
    return name;
  }
}
