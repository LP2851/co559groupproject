package ui;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the GUI class. Tests none GUI related methods like the validatePhoneNumber() method.
 * @author Lucas
 * @version 0.1
 */
class GUITest {

    // Variables used in the tests.
    static String validNumber = "01234567890";
    static String noNumber = "";
    static String tooShort = "01010101";
    static String tooLong = "09120873497542308947307";
    static String hasNonNumeric = "0123456789a";
    static GUI gui;

    /**
     * Creates a new GUI object before running the tests
     */
    @BeforeAll
    static void setUp() {
        gui = new GUI();
        gui.setVisible(false);
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

}