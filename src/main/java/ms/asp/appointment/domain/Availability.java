package ms.asp.appointment.domain;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("AVAILABILITY")
@EqualsAndHashCode(callSuper = true)
public class Availability extends BaseEntity {
    private boolean isAvailable;
    private AvailabilityType availabilityType;
}
