package ms.asp.appointment.model;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.domain.Contact;
import ms.asp.appointment.domain.ServiceType;
import ms.asp.appointment.domain.Slot;

@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceProviderModel extends BaseModel {

    private String subTitle;
    private String location;
    private Contact contact;
    private double price;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime start;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime end;
    private boolean fullDayReservation;
    private int ordersPerDaySlot;
    private boolean hasDescription;
    private Set<DayOfWeek> offDays;
    private Set<Slot> amSlot;
    private Set<Slot> pmSlot;
    private Set<ServiceType> serviceType;
}
