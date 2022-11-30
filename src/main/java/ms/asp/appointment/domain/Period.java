package ms.asp.appointment.domain;

import java.time.LocalDateTime;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("PERIOD")
@EqualsAndHashCode(callSuper = true)
public class Period extends BaseEntity {
    private LocalDateTime start;
    private LocalDateTime end;
}
