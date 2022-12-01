package ms.asp.appointment.util;

import java.util.UUID;

public class CommonUtils {

    public static String generatePublicId() {
	return UUID.randomUUID().toString().replace("-", "");
    }

    public static String value(String text, Object obj) {
	return String.format("%s=%s", text, obj.toString());
    }
}
