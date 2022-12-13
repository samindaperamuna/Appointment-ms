package ms.asp.appointment.util;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class CommonUtils {

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String TIME_FORMAT = "HH:mm";

    public static String generatePublicId() {
	return UUID.randomUUID().toString().replace("-", "");
    }

    public static String value(String text, Object obj) {
	return String.format("%s=%s", text, obj.toString());
    }

    public static boolean isValidDateTime(String dateTime) {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

	try {
	    formatter.parse(dateTime);
	} catch (Exception e) {
	    return false;
	}

	return true;
    }
}
