package ee.era.code.GateWeb;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeHelper {

    public static String formatTimestamp(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }
}
