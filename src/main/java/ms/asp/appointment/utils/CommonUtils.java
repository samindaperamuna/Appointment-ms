package ms.asp.appointment.utils;

import java.util.UUID;

public class CommonUtils {

  public static String generatePublicId(){
    return UUID.randomUUID().toString().replace("-", "");
  }
}
