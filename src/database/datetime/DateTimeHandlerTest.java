package database.datetime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing the DataTimeHandler class.
 * @author Lucas
 * @version 0.2
 */
class DateTimeHandlerTest {

    // Testing objects.
    DateTimeHandler dth1, dth2, dth3;
    Date d;

    /**
     * Creating the testing objects.
     */
    @BeforeEach
    void setUp() {
        dth1 = new DateTimeHandler(2021, 0, 1, 7, 0);
        d = new Date(2021-1900, Calendar.JANUARY, 1);
        d.setHours(7);
        dth2 = new DateTimeHandler(d);
        dth3 = new DateTimeHandler(3000, 0, 1, 7, 0);
    }

    /**
     * Testing that the constructor taking a string does not throw an error when passed a valid string.
     * Testing that the constructor taking a string does throw an error when passed an invalid string.
     */
    @Test
    void testParseStringConstructor() {
        assertDoesNotThrow(() -> new DateTimeHandler("2021-01-01 07:00:00.000"));
        assertThrows(ParseException.class, () -> new DateTimeHandler("asdsdaiuyasdfgiu"));
    }

    /**
     * Testing the checkDateIsAfterNow() method returns the correct values
     */
    @Test
    void checkDateIsAfterNow() {
        assertTrue(dth3.checkDateIsAfterNow());
        assertFalse(dth1.checkDateIsAfterNow());
        assertFalse(dth2.checkDateIsAfterNow());
    }

    /**
     * Testing the static version of the checkDateIsAfterNow() method.
     */
    @Test
    void testCheckDateIsAfterNow() {
        assertTrue(DateTimeHandler.checkDateIsAfterNow(dth3.getDate()));
        assertFalse(DateTimeHandler.checkDateIsAfterNow(dth1.getDate()));
        assertFalse(DateTimeHandler.checkDateIsAfterNow(dth2.getDate()));
    }

    /**
     * Testing the checkIsBefore() method returns the correct values.
     */
    @Test
    void checkIsBefore() {
        assertTrue(dth1.checkIsBefore(dth3));
        assertFalse(dth1.checkIsBefore(dth2));
    }

    /**
     * Testing the getDateFromValues() static method returns the correct values.
     */
    @Test
    void getDateFromValues() {
        assertEquals(d.toString(), DateTimeHandler.getDateFromValues(2021, 0, 1, 7, 0).toString());
    }

    /**
     * Testing the toString() method returns the correct values.
     */
    @Test
    void testToString() {
        assertEquals("2021-01-01 07:00:00.000", dth1.toString());
        assertEquals("2021-01-01 07:00:00.000", dth2.toString());
    }

    /**
     * Testing the getDate() method returns the correct values.
     */
    @Test
    void getDate() {
        assertEquals("2021-01-01 07:00:00.000", dth1.toString());
    }

    /**
     * Testing the isValidDateFromInputs() method returns the correct values.
     */
    @Test
    void isValidDateFromInputs() {
        assertFalse(DateTimeHandler.isValidDateFromInputs(31, 1, 2021));
        assertFalse(DateTimeHandler.isValidDateFromInputs(30, 1, 2021));
        assertFalse(DateTimeHandler.isValidDateFromInputs(29, 1, 2021));
        assertTrue(DateTimeHandler.isValidDateFromInputs(28, 1, 2021));
        assertTrue(DateTimeHandler.isValidDateFromInputs(29, 1, 2020));
        assertFalse(DateTimeHandler.isValidDateFromInputs(31, 3, 2021));

        assertFalse(DateTimeHandler.isValidDateFromInputs(0, 0, 2021));
        assertFalse(DateTimeHandler.isValidDateFromInputs(32, 1, 2021));
        assertFalse(DateTimeHandler.isValidDateFromInputs(1, -1, 2021));
        assertFalse(DateTimeHandler.isValidDateFromInputs(1, 12, 2021));
    }
}