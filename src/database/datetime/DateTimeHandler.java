package database.datetime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class for the DateTimeHandler
 * @author Lucas
 * @version 0.2
 */
public class DateTimeHandler {

    // String representation of the date
    private String dateString;
    // Date representation of the date.
    private Date date;
    // Format of date strings.
    protected static String DATE_FORMAT_STRING = "yyyy-MM-dd HH:mm:ss.sss";

    /**
     * Constructor for DateTimeHandler objects.
     * @param year The year
     * @param month The month
     * @param day The day
     * @param hour The hour
     * @param minute The minute
     */
    public DateTimeHandler(int year, int month, int day, int hour, int minute) {
        // Creates the date object from the passed values
        date = getDateFromValues(year, month, day, hour, minute);
        // Creates the date string from the date object
        dateString = getStringFromDate();
    }

    /**
     * Constructor for DateTimeHandler objects.
     * @param d Date object
     */
    public DateTimeHandler(Date d) {
        date = d;
        // Converts the date passed into a string
        dateString = getStringFromDate();
    }

    /**
     * Constructor for DateTimeHandler objects
     * @param s Date string
     * @throws ParseException Can error when the string isn't valid
     */
    public DateTimeHandler(String s) throws ParseException {
        dateString = s;
        date = getDateFromString();

    }

    /**
     * Returns a date object based on the date string of the current object
     * @return A date object based on the date string of the current object.
     * @throws ParseException Can error if the string is invalid
     */
    private Date getDateFromString() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STRING);
        return sdf.parse(dateString);
    }

    /**
     * Returns a string representing the date object.
     * @return A string representing the date object.
     */
    private String getStringFromDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STRING);
        return sdf.format(date);
    }

    /**
     * True if date in this object is after when this is called, false otherwise
     * @return True if date is after now, otherwise false.
     */
    public boolean checkDateIsAfterNow() {
        return date.after(new Date());
    }

    /**
     * True if date  is after when this is called, false otherwise
     * @param d Date to check against.
     * @return True if date is after now, otherwise false.
     */
    public static boolean checkDateIsAfterNow(Date d) {
        return d.after(getNow());
    }

    /**
     * Returns true if the current date is before the passed object.
     * @param dth A DateTimeHandler to be checked against
     * @return True if the current date is before the passed object.
     */
    public boolean checkIsBefore(DateTimeHandler dth) {
        return (date.before(dth.getDate()));
    }

    /**
     * Returns a date object based on the values passed
     * @param year The year
     * @param month The month
     * @param day The day
     * @param hour The hour
     * @param min The minute
     * @return A date objet based on the values passed
     */
    public static Date getDateFromValues(int year, int month, int day, int hour, int min) {
        Date d = new Date(year-1900, month, day);
        d.setHours(hour);
        d.setMinutes(min);
        return d;
    }

    /**
     * Returns the string representation of the date.
     * @return The string representation of the date.
     */
    @Override
    public String toString() {
        return dateString;
    }

    /**
     * Returns the date object
     * @return The date object
     */
    public Date getDate() {
        return date;
    }

    /**
     * Returns a date object for now.
     * @return A date object for now.
     */
    public static Date getNow() {
        return new Date();
    }

    /**
     * Returns if the date is valid (e.g. cannot have 31st of Feb)
     * @param day The day
     * @param month The month
     * @param year The year
     * @return If the date is valid
     */
    public static boolean isValidDateFromInputs(int day, int month, int year) {
        // FEB = 28/29 Days
        if (month == 1) {
            if (year % 4 == 0 && day > 29) {
                return false;
            } else if (year % 4 != 0 && day > 28) {
                return false;
            }
        // APR, JUN, SEPT, NOV = 30 days
        } else if (day == 31 && (month == 3 || month == 5 || month == 8 || month == 10)) {
            return false;
        }
        return (day > 0 && day < 32 &&
                month > -1 && month < 12);
    }

}
