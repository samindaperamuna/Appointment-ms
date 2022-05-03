package ms.asp.appointment.domain;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ms.asp.appointment.domain.listeners.FahirListener;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity
@Audited
@EntityListeners({FahirListener.class, AuditingEntityListener.class})
public class Appointement extends BaseEntity{

  private String status;
  private String cancellationReason;
  @OneToMany
  private Set<CodeableConcept> serviceCategory;
  @OneToMany
  private Set<CodeableConcept> serviceType;
  @OneToMany
  private Set<CodeableConcept> specialty;
  @OneToMany
  private Set<CodeableConcept> appointmentType;
  @OneToOne
  private Reason reasonCode;
  private int priority;
  private String description;
  @OneToMany
  private Set<Appointement> replaces;
  private String supportingInformation;
  private LocalDateTime start;
  private LocalDateTime end;
  private int minutesDuration;
  @OneToMany
  private Set<Slot> slot;
  @CreatedDate
  private LocalDateTime created;
  @LastModifiedDate
  private LocalDateTime modified;
  private String comment;
  private String patientInstruction;
  @OneToMany
  private Set<Note> note;
  @OneToMany
  private Set<Participant> participant;
  @OneToMany
  private Set<Period> requestedPeriod;

}
