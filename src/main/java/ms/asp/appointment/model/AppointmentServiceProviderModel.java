package ms.asp.appointment.model;

import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.util.CommonUtils;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppointmentServiceProviderModel extends BaseModel {

    private String subTitle;
    private String location;
    private ContactModel contact;
    private double price;

    @DateTimeFormat(pattern = CommonUtils.TIME_FORMAT)
    @JsonFormat(pattern = CommonUtils.TIME_FORMAT)
    private LocalTime start;

    @DateTimeFormat(pattern = CommonUtils.TIME_FORMAT)
    @JsonFormat(pattern = CommonUtils.TIME_FORMAT)
    private LocalTime end;

    private boolean active;
}
