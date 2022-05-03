package ms.asp.appointment.domain;

import javax.persistence.Entity;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public class Reason extends CodeableConcept{
}
