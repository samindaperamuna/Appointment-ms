package ms.asp.appointment.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.util.CommonUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class PeriodModel extends BaseModel {
    @DateTimeFormat(pattern = CommonUtils.DATE_TIME_FORMAT)
    @JsonFormat(pattern = CommonUtils.DATE_TIME_FORMAT)
    private LocalDateTime start;

    @DateTimeFormat(pattern = CommonUtils.DATE_TIME_FORMAT)
    @JsonFormat(pattern = CommonUtils.DATE_TIME_FORMAT)
    private LocalDateTime end;
}
