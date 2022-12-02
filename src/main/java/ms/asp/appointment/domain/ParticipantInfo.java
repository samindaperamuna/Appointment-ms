package ms.asp.appointment.domain;

import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("PARTICIPANT_INFO")
@EqualsAndHashCode(callSuper = true)
public class ParticipantInfo extends BaseEntity {

    private String name;

    @Transient
    private Contact contact;
    private Long contactId;

    @Override
    public String toString() {
	return name;
    }
}
