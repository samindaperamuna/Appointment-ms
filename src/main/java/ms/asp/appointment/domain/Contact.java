package ms.asp.appointment.domain;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Table(name = "CONTACTS")
@Entity
public class Contact extends BaseEntity{
  private String location;
  private String telephone;
  private String email;
}
