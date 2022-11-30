package ms.asp.appointment.util;

import java.util.UUID;

public class CommonUtils {

    public static String generatePublicId() {
	return UUID.randomUUID().toString().replace("-", "");
    }
}
