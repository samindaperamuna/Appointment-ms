package ms.asp.appointment.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeriodModel {
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = ISO.DATE_TIME)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime start;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = ISO.DATE_TIME)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime end;
}
