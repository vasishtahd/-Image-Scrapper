package appengine.parser.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class TimeUtils {

    public static Timestamp getCurrentTime() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        String result = sdf.format(calendar.getTime());
        Timestamp timestamp = new Timestamp(calendar.getTimeInMillis());
        return timestamp;
    }
}
