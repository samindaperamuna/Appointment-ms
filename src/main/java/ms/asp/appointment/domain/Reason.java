package ms.asp.appointment.domain;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("REASON")
@EqualsAndHashCode(callSuper = true)
public class Reason extends BaseEntity {

    private String system;
    private String code;
    private String display;

    public String toString() {
	return display;
    }
}
