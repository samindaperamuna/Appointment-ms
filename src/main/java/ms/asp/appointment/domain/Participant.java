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

    private String type;
    private boolean required;
    private String status;

    @Transient
    private Appointment appointment;
    private Long appointmentId;

    @Transient
    private ParticipantInfo participantInfo;
    private Long participantInfoId;

    @Override
    public String toString() {
	return Objects.toString(participantInfo);
    }
}
