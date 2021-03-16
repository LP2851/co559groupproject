package database.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests the Doctor class
 * @author Lucas
 * @version 0.2
 */
class DoctorTest {

    // Doctor object
    Doctor d1;

    /**
     * Creating objects for testing.
     */
    @BeforeEach
    void setUp() {
        d1 = new Doctor(1, "Test", "Doc", "01234567890", "GP");
    }

    /**
     * Tests the getDoctorFromID() method returns the correct values.
     */
    @Test
    void getDoctorFromID() {
        assertEquals(d1, Doctor.getDoctorFromID(1));
    }

    /**
     * Tests the getDoctorFromString() method returns the correct values.
     */
    @Test
    void getDoctorFromString() {
        assertEquals(d1, Doctor.getDoctorFromString("Test Doc (GP)"));
    }

    /**
     * Tests the getBackground() method returns the correct values.
     */
    @Test
    void getBackground() {
        assertEquals("GP", d1.getBackground());
    }

    /**
     * Tests the setBackground() method stores the correct values.
     */
    @Test
    void setBackground() {
        d1.setBackground("Background");
        assertEquals("Background", d1.getBackground());
    }

    /**
     * Tests the toString() method returns the correct values.
     */
    @Test
    void testToString() {
        assertEquals("Test Doc (GP)", d1.toString());
    }
}