package ms.asp.appointment.domain;

import java.time.LocalDateTime;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("NOTE")
@EqualsAndHashCode(callSuper = true)
public class Note extends BaseEntity {
    private String author;
    private LocalDateTime time;
    private String text;
}
