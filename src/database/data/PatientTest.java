package database.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests the Patient class
 * @author Lucas
 * @version 0.2
 */
class PatientTest {
    // Testing objects
    Patient p1, p2;
    Doctor d1;

    /**
     * Creating test objects for tests.
     */
    @BeforeEach
    void setUp() {
        p1 = new Patient(1, "0123456789", "Test", "Person", "", 0);
        p2 = new Patient(2, "9876543210", "Tester2", "Person", "", 1);
        d1 = new Doctor(1, "Test", "Doc", "", "");
    }

    /**
     * Tests the getPatientFromID() method returns the correct values.
     */
    @Test
    void getPatientFromID() {
        assertEquals(p1, Patient.getPatientFromID(1));
        assertEquals(p2, Patient.getPatientFromID(2));
    }

    /**
     * Testing the getPatientFromNHSNumber() method returns the correct values.
     */
    @Test
    void getPatientFromNHSNumber() {
        assertEquals(p1, Patient.getPatientFromNHSNumber("0123456789"));
        assertEquals(p2, Patient.getPatientFromNHSNumber("9876543210"));

    }

    /**
     * Testing the getDoctorID() method returns the correct values.
     */
    @Test
    void getDoctorID() {
        assertEquals(0, p1.getDoctorID());
        assertEquals(1, p2.getDoctorID());
    }

    /**
     * Testing the setDoctorID() method sets the correct values
     */
    @Test
    void setDoctorID() {
        p1.setDoctorID(1);
        assertEquals(1, p1.getDoctorID());
    }

    /**
     * Tests the getNhsNumber() method returns the correct values.
     */
    @Test
    void getNhsNumber() {
        assertEquals("0123456789", p1.getNhsNumber());
        assertEquals("9876543210", p2.getNhsNumber());
    }

    /**
     * Tests the setNhsNumber() method sets the correct values.
     */
    @Test
    void setNhsNumber() {
        p1.setNhsNumber("0000000000");
        assertEquals("0000000000", p1.getNhsNumber());
    }

    /**
     * Tests the toString() method returns the correct values.
     */
    @Test
    void testToString() {
        assertEquals("Test Person (0123456789)", p1.toString());
    }

    /**
     * Tests the getDoctor() method returns the correct values.
     */
    @Test
    void getDoctor() {
        assertEquals(d1.getId(), p2.getDoctor().getId());
        assertNull(p1.getDoctor());
    }

    /**
     * Tests the setDoctor() method sets the correct values.
     */
    @Test
    void setDoctor() {
        p1.setDoctor(d1);
        assertEquals(d1, p1.getDoctor());
    }

    /**
     * Tests the getAllDetailsString() method returns the correct values.
     */
    @Test
    void getAllDetailsString() {
        String test =   "NHS Number: " + "9876543210" + "\n" +
                        "Patient Name: " + "Tester2 Person" + "\n" +
                        "Patient Phone Number: " + "" + "\n" +
                        "Usual Doctor: " + d1.toString();
        assertEquals(test, p2.getAllDetailsString());
    }
}