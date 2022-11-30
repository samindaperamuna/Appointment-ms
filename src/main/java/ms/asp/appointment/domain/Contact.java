package ms.asp.appointment.domain;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("CONTACT")
@EqualsAndHashCode(callSuper = true)
public class Contact extends BaseEntity {
    private String location;
    private String telephone;
    private String email;
}
