package ms.asp.appointment.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.domain.ServiceType;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceProviderModel extends BaseModel {

    private String subTitle;
    private String location;
    private ContactModel contact;
    private double price;
    private boolean active;

    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime start;

    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime end;

    private boolean fullDayReservation;
    private int ordersPerDaySlot;
    private boolean hasDescription;

    private List<DayOfWeek> offDays;
    private List<SlotModel> amSlots;
    private List<SlotModel> pmSlots;
    private List<ServiceType> serviceTypes;
    private List<AvailabilityModel> availability;
}
