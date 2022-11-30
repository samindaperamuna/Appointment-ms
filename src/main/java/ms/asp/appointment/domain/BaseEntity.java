package ms.asp.appointment.domain;

import static ms.asp.appointment.util.CommonUtils.generatePublicId;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import lombok.Data;

@Data
public abstract class BaseEntity implements Serializable {

    @Id	
    private Long id;

    @Column("public_id")
    private String publicId;

    public BaseEntity() {
	publicId = generatePublicId();
    }
}
