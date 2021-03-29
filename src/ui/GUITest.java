package ui;

import database.data.Booking;
import database.data.Doctor;
import database.data.Patient;
import database.datetime.DateTimeHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the GUI class. Tests none GUI related methods like the validatePhoneNumber() method.
 * @author Lucas
 * @version 0.3
 */
class GUITest {

    // Variables used in the tests.
    static String validNumber = "01234567890";
    static String noNumber = "";
    static String tooShort = "01010101";
    static String tooLong = "09120873497542308947307";
    static String hasNonNumeric = "0123456789a";
    static GUI gui;
    static Booking b1;
    static Patient p1, p2;
    static Doctor d1, d2;

    /**
     * Creates a new GUI object before running the tests
     */
    @BeforeAll
    static void setUp() {
        gui = new GUI();
        gui.setVisible(false);
        p1 = new Patient(1, "0123456789", "Test", "Surname", "", 0);
        p2 = new Patient(2, "9876543210", "Test", "Patient", "", 0);
        d1 = new Doctor(1, "Test", "Doctor", "", "");
        d2 = new Doctor(2, "Test", "Doctor2", "", "2");
        b1 = new Booking(new DateTimeHandler(2025, 12, 21, 8, 0),
                new DateTimeHandler(2025, 12, 21, 9, 0),
                p1, d1);

    }

    /**
     * When a valid number is inputted, true is returned.
     * A valid number is a number containing 11 numeric characters or is empty
     */
    @Test
    void validatePhoneNumber1() {
        assertTrue(gui.validatePhoneNumber(validNumber));
    }

    /**
     * When an empty number is inputted, true is returned.
     * A valid number is a number containing 11 numeric characters or is empty
     */
    @Test
    void validatePhoneNumber2() {
        assertTrue(gui.validatePhoneNumber(noNumber));
    }

    /**
     * When a number that is too short is inputted, false is returned
     * A valid number is a number containing 11 numeric characters or is empty
     */
    @Test
    void validatePhoneNumber3() {
        assertFalse(gui.validatePhoneNumber(tooShort));
    }

    /**
     * When a number that is too long is inputted, false is returned
     * A valid number is a number containing 11 numeric characters or is empty
     */
    @Test
    void validatePhoneNumber4() {
        assertFalse(gui.validatePhoneNumber(tooLong));
    }

    /**
     * When a number containing a non numeric character is inputted, false is returned
     * A valid number is a number containing 11 numeric characters or is empty
     */
    @Test
    void validatePhoneNumber5() {
        assertFalse(gui.validatePhoneNumber(hasNonNumeric));
    }

    /**
     * When a valid number is inputted, true is returned.
     * A valid number is a number containing 10 numeric characters.
     * An empty number is also excepted ana handled elsewhere in the program.
     */
    @Test
    void validateNHSNumber1() {
        assertTrue(gui.validateNHSNumber("0123456789"));
    }

    /**
     * When a number that is too short is inputted, false is returned.
     * A valid number is a number containing 10 numeric characters.
     * An empty number is also excepted ana handled elsewhere in the program.
     */
    @Test
    void validateNHSNumber2() {
        assertFalse(gui.validateNHSNumber(tooShort));
    }

    /**
     * When a number that is too long is inputted, false is returned.
     * A valid number is a number containing 10 numeric characters.
     * An empty number is also excepted ana handled elsewhere in the program.
     */
    @Test
    void validateNHSNumber3() {
        assertFalse(gui.validateNHSNumber(tooLong));
    }

    /**
     * When a number that contains a non-numeric character is inputted, false is returned.
     * A valid number is a number containing 10 numeric characters.
     * An empty number is also excepted ana handled elsewhere in the program.
     */
    @Test
    void validateNHSNumber4() {
        assertFalse(gui.validateNHSNumber(hasNonNumeric));
    }

    /**
     * When an empty string is inputted, true is returned.
     * A valid number is a number containing 10 numeric characters.
     * An empty number is also excepted ana handled elsewhere in the program.
     */
    @Test
    void validateNHSNumber5() {
        assertTrue(gui.validateNHSNumber(""));
    }

    /**
     * When there is no filter on then the booking is accepted.
     */
    @Test
    void passesFilter1() {
        // No Filter
        assertTrue(gui.passesFilter(b1));
    }

    /**
     * When there is a booking with a patient filter, the filter gets rid of bookings with the
     * incorrect patient.
     */
    @Test
    void passesFilter2() {
        // Patient Filter- True
        gui.patientFilter = p1;
        assertTrue(gui.passesFilter(b1));
        // Patient Filter- False
        gui.patientFilter = p2;
        assertFalse(gui.passesFilter(b1));
    }

    /**
     * When there is a booking with a doctor filter, the filter gets rid of bookings with the
     * incorrect doctor.
     */
    @Test
    void passesFilter3() {
        // Doctor Filter- True
        gui.doctorFilter = d1;
        assertTrue(gui.passesFilter(b1));
        // Doctor Filter- False
        gui.doctorFilter = d2;
        assertFalse(gui.passesFilter(b1));
    }

    /**
     * Tear down.
     */
    @AfterEach
    void tearDown() {
        gui.patientFilter = null;
        gui.doctorFilter = null;
        gui.filteringDateTime = false;
    }
}