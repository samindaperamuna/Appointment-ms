package ms.asp.appointment.domain;

import java.util.Set;

import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("SCHEDULE")
@EqualsAndHashCode(callSuper = true)
public class Schedule extends BaseEntity {

    private boolean active;
    private String comment;

    @Transient
    private Set<ParticipantInfo> participantInfo;
    @Transient
    private Period planningHorizon;
}
