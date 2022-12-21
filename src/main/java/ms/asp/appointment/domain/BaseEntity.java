package ms.asp.appointment.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public abstract class BaseEntity implements Serializable {

    @Id
    private Long id;

    private String publicId;
}
