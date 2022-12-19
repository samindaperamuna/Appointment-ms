package ms.asp.appointment.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.util.CommonUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class SlotModel extends BaseModel {

    private String status;

    @DateTimeFormat(pattern = CommonUtils.TIME_FORMAT)
    @JsonFormat(pattern = CommonUtils.TIME_FORMAT)
    private LocalTime start;

    @DateTimeFormat(pattern = CommonUtils.TIME_FORMAT)
    @JsonFormat(pattern = CommonUtils.TIME_FORMAT)
    private LocalTime end;

    private ServiceProviderModel serviceProvider;

    private List<DayOfWeek> validDays;
    private boolean wholeWeek;

    private List<AvailabilityModel> availability;
}
