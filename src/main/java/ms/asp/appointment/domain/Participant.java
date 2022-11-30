package ms.asp.appointment.domain;

import java.util.Objects;

import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("PARTICIPANT")
@EqualsAndHashCode(callSuper = true)
public class Participant extends BaseEntity {

    public String type;
    public boolean required;
    public String status;

    @Transient
    public Period period;
    @Transient
    public ParticipantInfo participantInfo;

    @Override
    public String toString() {
	return Objects.toString(participantInfo);
    }
}
