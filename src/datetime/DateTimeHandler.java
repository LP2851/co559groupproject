package datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeHandler {

    private String dateString;
    private Date date;
    protected static String DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss.sss";

    public DateTimeHandler(int year, int month, int day, int hour, int minute) {
        date = getDateFromValues(year, month, day, hour, minute);
        dateString = getStringFromDate();
    }

    public DateTimeHandler(Date d) {
        date = d;
        dateString = getStringFromDate();
    }

    public DateTimeHandler(String s) throws ParseException {
        dateString = s;
        date = getDateFromString();

    }

    private Date getDateFromString() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STRING);
        return sdf.parse(dateString);
    }

    private String getStringFromDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STRING);
        //System.out.println(sdf.format(date));
        return sdf.format(date);
    }

    public boolean checkDateIsAfterNow() {
        return date.after(new Date());
    }

    public static boolean checkDateIsAfterNow(Date d) {
        return d.after(new Date());
    }

    public static Date getDateFromValues(int year, int month, int day, int hour, int min) {
        Date d = new Date(year-1900, month-1, day);
        d.setHours(hour);
        d.setMinutes(min);
        return d;
    }

    @Override
    public String toString() {
        return dateString;
    }

    public Date getDate() {
        return date;
    }

}
