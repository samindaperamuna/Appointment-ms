package ms.asp.appointment.domain;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("AVAILABILITY")
@EqualsAndHashCode(callSuper = true)
public class Availability extends BaseEntity {
    private AvailabilityType availabilityType;
    private boolean available;
}
