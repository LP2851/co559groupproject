package datetime;

import java.util.ArrayList;

public class DateTimeAuthentication {

    public static boolean isValidBooking(ArrayList<StartEndPair> doctorsBookings, StartEndPair booking) {

        for (int i = 0; i < doctorsBookings.size(); i++) {
            StartEndPair pair = doctorsBookings.get(i);
            // If booking starts before and doesn't end before the start of booked booking
            if (booking.getStart().before(pair.getStart()) && !booking.getEnd().before(pair.getStart())) {
                return false;
            // Booking starts after and not after end of booked booking
            } else if (booking.getStart().after(pair.getStart()) && !booking.getStart().after(pair.getEnd())){
                return false;
            }
        }
        return true;
    }

}
