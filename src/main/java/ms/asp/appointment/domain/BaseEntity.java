package ms.asp.appointment.domain;

import static javax.persistence.GenerationType.IDENTITY;
import static ms.asp.appointment.utils.CommonUtils.generatePublicId;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.Data;
import org.hibernate.envers.Audited;

@Data
@MappedSuperclass
@Audited
public abstract class BaseEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "PUBLIC_ID", unique = true)
  private String publicId;

  @Version private long version;
  public BaseEntity(){
    publicId = generatePublicId();
  }
}
