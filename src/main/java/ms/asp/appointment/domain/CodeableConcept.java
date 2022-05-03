package ms.asp.appointment.domain;


import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Audited
@Entity
@Table(name="CODEABLE_CONCEPT")
public class CodeableConcept extends BaseEntity {

  private String system;
  private String code;
  private String display;

  public String toString(){
    return display;
  }
}
