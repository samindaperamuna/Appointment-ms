package ms.asp.appointment.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("SERVICE_PROVIDER")
@EqualsAndHashCode(callSuper = true)
public class ServiceProvider extends AuditedEntity {

    private String subTitle;
    private String location;
    private double price;
    private LocalTime start;
    private LocalTime end;
    private boolean fullDayReservation;
    private int ordersPerDaySlot;
    private boolean hasDescription;
    private boolean active;

    @Transient
    private Set<ServiceType> serviceTypes;
    private String serviceTypesJSON;

    @Transient
    private Set<DayOfWeek> offDays;
    private String offDaysJSON;

    @Transient
    private Contact contact;
    private Long contactId;

    @Transient
    private Set<Slot> amSlots;

    @Transient
    private Set<Slot> pmSlots;

    @Transient
    private Set<Availability> availability;
}
