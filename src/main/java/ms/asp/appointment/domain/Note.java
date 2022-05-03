package ms.asp.appointment.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

@EqualsAndHashCode(callSuper = true)
@Table
@Entity
@Data
@Audited
@NoArgsConstructor
public class Note extends BaseEntity{
   private String author;
   private LocalDateTime time;
   private String text;
}
