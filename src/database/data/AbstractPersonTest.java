package database.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for AbstractPerson
 * @author Lucas
 * @version 0.2
 */
class AbstractPersonTest {

    // Test people
    AbstractPerson d1, d2, d3;

    /**
     * Creates doctor objects before each test.
     */
    @BeforeEach
    void setUp() {
        d1 = new Doctor(1, "Test", "Doc", "01234567890", "GP");
        d2 = new Doctor(3, "T2", "D2", "", "");
        d3 = new Doctor(4, "Abcd", "Efg", "", "");
    }

    /**
     * Checks the getFullName() method returns the correct values.
     */
    @Test
    void getFullName() {
        assertEquals("Test Doc", d1.getFullName());
    }

    /**
     * Tests the getId() method returns the correct values.
     */
    @Test
    void getId() {
        assertEquals(1, d1.getId());
    }

    /**
     * Tests the setId() method to make sure that the values are being set correctly.
     */
    @Test
    void setId() {
        d1.setId(2);
        assertEquals(2, d1.getId());
    }

    /**
     * Tests the getFname() method returns the correct values.
     */
    @Test
    void getFname() {
        assertEquals("Test", d1.getFname());
    }

    /**
     * Tests the setFname() method to make sure the values are being set correctly.
     */
    @Test
    void setFname() {
        d1.setFname("Tester");
        assertEquals("Tester", d1.getFname());
    }

    /**
     * Tests the getSname() method returns the correct values.
     */
    @Test
    void getSname() {
        assertEquals("Doc", d1.getSname());
    }

    /**
     * Tests the setSname() method to make sure the values are being set correctly.
     */
    @Test
    void setSname() {
        d1.setSname("Doctor");
        assertEquals("Doctor", d1.getSname());
    }

    /**
     * Tests the getPhone() method returns the correct values.
     */
    @Test
    void getPhone() {
        assertEquals("01234567890", d1.getPhone());
    }

    /**
     * Tests the setPhone() method to make sure the values are being set correctly.
     */
    @Test
    void setPhone() {
        d1.setPhone("09876543210");
        assertEquals("09876543210", d1.getPhone());
    }

    /**
     * Tests the toArray() method to make sure the array is in the correct order and contains the
     * correct elements.
     */
    @Test
    void toArray() {
        ArrayList<AbstractPerson> list = new ArrayList<>();
        list.add(d1); list.add(d2); list.add(d3);
        assertArrayEquals(new String[] {"Abcd Efg", "T2 D2", "Test Doc (GP)"}, AbstractPerson.toArray(list));
    }
}