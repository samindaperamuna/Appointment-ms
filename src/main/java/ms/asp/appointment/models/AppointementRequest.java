package ms.asp.appointment.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ms.asp.appointment.domain.Reason;
import ms.asp.appointment.domain.Slot;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointementRequest extends BaseModel{
  private String status;
  private String cancellationReason;
  private Set<String> serviceCategory;
  private Set<String> serviceType;
  private Set<String> specialty;
  private Set<String> appointmentType;
  private Reason reasonCode;
  private int priority;
  private String description;
  private Set<AppointmentsHistoryResponse> replaces;
  private String supportingInformation;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = ISO.DATE_TIME)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime start;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = ISO.DATE_TIME)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
  private LocalDateTime end;
  private int minutesDuration;
  private Set<Slot> slot;
  private LocalDateTime created;
  private LocalDateTime modified;
  private String comment;
  private String patientInstruction;
  private Set<NoteModel> note;
  private Set<String> participant;
  private Set<PeriodModel> requestedPeriod;
}
