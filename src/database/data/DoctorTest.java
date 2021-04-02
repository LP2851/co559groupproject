package database.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the Doctor class
 * @author Lucas
 * @version 0.3
 */
class DoctorTest {

    // Doctor object
    Doctor d1;

    /**
     * Creating objects for testing.
     */
    @BeforeEach
    void setUp() {
        Doctor.doctorIDMap.clear();
        Doctor.doctorStringMap.clear();
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

    /**
     * Checks that the doctors added are the only things in the return from getAllDoctors()
     * in the order they were added.
     */
    @Test
    void getAllDoctors() {
        assertArrayEquals(new Doctor[] {d1}, Doctor.getAllDoctors());
        Doctor d2 = new Doctor(2, "Other", "Doc", "98765432100", "");
        assertArrayEquals(new Doctor[] {d1, d2}, Doctor.getAllDoctors());
    }
}