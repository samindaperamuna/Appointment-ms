package ms.asp.appointment.domain;

import java.util.List;

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
    private List<ParticipantInfo> participantInfo;
    @Transient
    private Period planningHorizon;
}
