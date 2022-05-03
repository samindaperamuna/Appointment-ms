package ms.asp.appointment.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CodeableConceptModel extends BaseModel{
  private String system;
  private String code;
  private String display;}
