package database.datetime;

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
        return sdf.format(date);
    }

    public boolean checkDateIsAfterNow() {
        return date.after(new Date());
    }

    public static boolean checkDateIsAfterNow(Date d) {
        return d.after(getNow());
    }

    public boolean checkIsBefore(DateTimeHandler dth) {
        return (date.before(dth.getDate()));
    }

    public static Date getDateFromValues(int year, int month, int day, int hour, int min) {
        Date d = new Date(year-1900, month, day);
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

    public static Date getNow() {
        return new Date();
    }

    public static boolean isValidDateFromInputs(int day, int month, int year) {
        // FEB = 28/29 Days
        if (month == 1) {
            if (year % 4 == 0 && day > 29) {
                return false;
            } else if (day > 28) {
                return false;
            }
        // APR, JUN, SEPT, NOV = 30 days
        } else if (day == 31 && (month == 3 || month == 5 || month == 8 || month == 10)) {
            return false;
        }
        return true;
    }
}
