package ms.asp.appointment.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CodeableConceptModel extends BaseModel {
    private String system;
    private String code;
    private String display;
}
