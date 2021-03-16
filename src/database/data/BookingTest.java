package database.data;

import database.datetime.DateTimeHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the Booking class
 * @author Lucas
 * @version 0.2
 */
class BookingTest {

    // All of the objects to be used in tests.
    Booking booking1, booking2, booking3, booking4;
    Patient p1, p2;
    Doctor d1, d2;
    DateTimeHandler date1, date2, date3;

    /**
     * Creating the objects for each of the tests.
     */
    @BeforeEach
    void setUp() {
        date1 = new DateTimeHandler(2021, 1, 1, 7, 0);
        date2 = new DateTimeHandler(2021, 1, 1, 8, 0);
        date3 = new DateTimeHandler(2021, 1, 1, 9, 0);

        p1 = new Patient(1, "0123456789", "Test", "Person", "", 1);
        p2 = new Patient(2, "9876543210", "T2", "Person", "", 2);
        d1 = new Doctor(1, "Test", "Doctor", "", "GP");
        d2 = new Doctor(2, "TestDoc", "Doctor", "", "");

        booking1 = new Booking(1, date1.getDate(), date2.getDate(), p1, d1);
        booking1.addToMap();
        booking2 = new Booking(2, date2.getDate(), date3.getDate(), p2, d2);
        booking3 = new Booking(3, date1.getDate(), date2.getDate(), p1, d2);
        booking4 = new Booking(4, date1.getDate(), date2.getDate(), p2, d1);
    }

    /**
     * Testing the getPatient() method returns the correct values.
     */
    @Test
    void getPatient() {
        assertEquals(p1, booking1.getPatient());
    }

    /**
     * Testing the getDoctor() method returns the correct values.
     */
    @Test
    void getDoctor() {
        assertEquals(d1, booking1.getDoctor());
    }

    /**
     * Testing the getId() method returns the correct values.
     */
    @Test
    void getId() {
        assertEquals(1, booking1.getId());
    }

    /**
     * Testing the getStartDateTime() method returns the correct values.
     */
    @Test
    void getStartDateTime() {
        assertEquals(date1.toString(), booking1.getStartDateTime().toString());
    }

    /**
     * Testing the setStartDateTime() method sets the correct values.
     */
    @Test
    void setStartDateTime() {
        booking1.setStartDateTime(date2);
        assertEquals(date2, booking1.getStartDateTime());
    }

    /**
     * Testing getEndDateTime() method returns the correct values.
     */
    @Test
    void getEndDateTime() {
        assertEquals(date2.toString(), booking1.getEndDateTime().toString());
    }

    /**
     * Testing setEndDateTime() method sets the correct values.
     */
    @Test
    void setEndDateTime() {
        booking1.setEndDateTime(date1);
        assertEquals(date1, booking1.getEndDateTime());
    }

    /**
     * Testing the getStartDateTimeAsDate() method returns the correct values.
     */
    @Test
    void getStartDateTimeAsDate() {
        assertEquals(date1.getDate(), booking1.getStartDateTimeAsDate());
    }

    /**
     * Testing the getEndDateTimeAsDate() method returns the correct values.
     */
    @Test
    void getEndDateTimeAsDate() {
        assertEquals(date2.getDate(), booking1.getEndDateTimeAsDate());
    }

    /**
     * Test the toString() method returns the correct values.
     */
    @Test
    void testToString() {
        String test =   "Patient: " + p1 + "\n" +
                        "Doctor: " + d1 + "\n" +
                        "Start: " + date1.toString() + "\n" +
                        "End: " + date2.toString();

        assertEquals(test, booking1.toString());
    }

    /**
     * Tests the authenticateBooking() method returns AUTHORISED when there isn't a booking clash.
     */
    @Test
    void authenticateBooking1() {
        assertEquals(Booking.AuthAnswer.AUTHORISED, Booking.authenticateBooking(booking2));
    }

    /**
     * Tests the authenticateBooking() method returns PATIENT_CLASH when there is a patient booking clash.
     */
    @Test
    void authenticateBooking2() {
        assertEquals(Booking.AuthAnswer.PATIENT_CLASH, Booking.authenticateBooking(booking3));
    }

    /**
     * Tests the authenticateBooking() method returns DOCTOR_CLASH when there is a doctor booking clash.
     */
    @Test
    void authenticateBooking3() {
        assertEquals(Booking.AuthAnswer.DOCTOR_CLASH, Booking.authenticateBooking(booking4));
    }
}