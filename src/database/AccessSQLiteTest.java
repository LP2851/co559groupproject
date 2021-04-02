package database;

import database.data.AbstractPerson;
import database.data.Booking;
import database.data.Doctor;
import database.data.Patient;
import database.datetime.DateTimeHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the AccessSQLite.
 * @author Lucas
 * @version 0.2
 */
class AccessSQLiteTest {

    // Object to connect to database
    private AccessSQLite accessSQLite;

    /**
     * Recreates table and data in the tables before each test is run.
     */
    @BeforeEach
    void setUp() {
        // Creates DB
        accessSQLite = new AccessSQLite("testing.db");

        // Creating Tables
        // Administrator table
        accessSQLite.runUpdateCommand("CREATE TABLE administrator (" +
                    "aid INTEGER," +
                    "fname VARCHAR(45)," +
                    "sname VARCHAR(45)," +
                    "username VARCHAR(45) NOT NULL UNIQUE, " +
                    "password VARCHAR(45) NOT NULL," +
                    "PRIMARY KEY (aid AUTOINCREMENT) );"
        );
        // Doctor Table
        accessSQLite.runUpdateCommand(
                    "CREATE TABLE doctor (" +
                    "did INTEGER," +
                    "fname VARCHAR(45)," +
                    "sname VARCHAR(45)," +
                    "phone VARCHAR(11), " +
                    "background TEXT," +
                    "PRIMARY KEY (did AUTOINCREMENT) );"
        );
        // Messages Table
        accessSQLite.runUpdateCommand(
                "CREATE TABLE messages (" +
                    "mid INTEGER," +
                    "aid INTEGER NOT NULL," +
                    "message TEXT NOT NULL," +
                    "FOREIGN KEY (aid) REFERENCES administrator (aid) ON DELETE CASCADE," +
                    "PRIMARY KEY (mid AUTOINCREMENT) );"
        );

        // Patient Table
        accessSQLite.runUpdateCommand(
                "CREATE TABLE patient (" +
                        "pid INTEGER," +
                        "nhsnumber VARCHAR(10) NOT NULL UNIQUE," +
                        "fname TEXT NOT NULL," +
                        "sname TEXT NOT NULL," +
                        "phone VARCHAR(11)," +
                        "doctor INTEGER," +
                        "FOREIGN KEY (doctor) REFERENCES doctor (did)," +
                        "PRIMARY KEY (pid AUTOINCREMENT) );"
        );

        // Bookings Table
        accessSQLite.runUpdateCommand(
                "CREATE TABLE booking (" +
                        "bid INTEGER," +
                        "startDateTime TEXT NOT NULL," +
                        "endDateTime TEXT NOT NULL," +
                        "doctor INTEGER NOT NULL," +
                        "patient INTEGER NOT NULL," +
                        "FOREIGN KEY (doctor) REFERENCES doctor (did)," +
                        "FOREIGN KEY (patient) REFERENCES patient (pid)," +
                        "UNIQUE (startDateTime, endDateTime, doctor)," +
                        "PRIMARY KEY (bid AUTOINCREMENT), " +
                        "UNIQUE (startDateTime, endDateTime, patient) );"
        );

        // Messages for Doctors Table
        accessSQLite.runUpdateCommand(
                "CREATE TABLE messagesdoctor (" +
                        "mid INTEGER, " +
                        "did INTEGER NOT NULL," +
                        "message TEXT NOT NULL," +
                        "FOREIGN KEY (did) REFERENCES doctor (did)," +
                        "PRIMARY KEY (mid AUTOINCREMENT) );"
        );

        // Messages for Patients Table
        accessSQLite.runUpdateCommand(
                "CREATE TABLE messagespatient (" +
                        "mid INTEGER, " +
                        "pid INTEGER NOT NULL," +
                        "message TEXT NOT NULL," +
                        "FOREIGN KEY (pid) REFERENCES patient (pid)," +
                        "PRIMARY KEY (mid AUTOINCREMENT) );"
        );

        // Populating the Database
        // Adding an administrator record
        accessSQLite.runUpdateCommand(
                "INSERT INTO administrator (fname, sname, username, password)" +
                    "VALUES ('Test', 'Person', 'tester1', 'test');"
        );
        // Adding an administrator record
        accessSQLite.runUpdateCommand(
                "INSERT INTO administrator (fname, sname, username, password)" +
                    "VALUES ('Other', 'Person', 'tester2', 'test');"
        );
        // Adding an administrator record
        accessSQLite.runUpdateCommand(
                "INSERT INTO administrator (fname, sname, username, password)" +
                    "VALUES ('Another', 'Person', 'tester4', 'test');"
        );
        // Adding a message record
        accessSQLite.runUpdateCommand(
                "INSERT INTO messages (aid, message)" +
                    "VALUES (1, 'This message is a test.');"
        );
        // Adding a message record
        accessSQLite.runUpdateCommand(
                "INSERT INTO messages (aid, message)" +
                    "VALUES (1, 'This message is another test.');"
        );
        // Adding a message record
        accessSQLite.runUpdateCommand(
                "INSERT INTO messages (aid, message)" +
                    "VALUES (2, 'This message is the final test.');"
        );
        // Adding doctor 1
        accessSQLite.runUpdateCommand(
                "INSERT INTO doctor (fname, sname, phone, background)" +
                    "VALUES ('Test', 'Doc', '', '');"
        );
        // Adding patient 1
        accessSQLite.runUpdateCommand(
                "INSERT INTO patient (nhsnumber, fname, sname, phone, doctor) " +
                        "VALUES ('9876543210', 'Test', 'Patient', '', 1);"
        );
        // Adding booking 1
        accessSQLite.runUpdateCommand(
                "INSERT INTO booking (startDateTime, endDateTime, doctor, patient)" +
                        "VALUES ('2021-01-01 07:00:00.000', '2021-01-01 08:00:00.000', 1, 1);"
        );
    }

    /**
     * When given a correct username and password, does it return the correct name of the user.
     */
    @Test
    void checkUsernamePassword1() {
        assertEquals("Test Person", accessSQLite.checkUsernamePassword("tester1", "test"));
    }

    /**
     * When given correct username, but wrong password, does it return an empty string due to failure to find
     * value in database.
     */
    @Test
    void checkUsernamePassword2() {
        assertEquals("", accessSQLite.checkUsernamePassword("tester1", "notCorrect"));
    }

    /**
     * When given the a username that doesn't exist, returns an empty string due to failure to find
     * value in database
     */
    @Test
    void checkUsernamePassword3() {
        assertEquals("", accessSQLite.checkUsernamePassword("tester3", "test"));
    }

    /**
     * When a doctor is created, it successfully gets added to the database.
     * Creates the following doctor:
     *  - aid       = SET BY DATABASE (should be 2)
     *  - fname     = "Doctor"
     *  - sname     = "Test"
     *  - phone     = "01234567890"
     *  - background= "GP"
     */
    @Test
    void addDoctor() {
        assertTrue(accessSQLite.addDoctor("Doctor", "Test", "01234567890", "GP"));
    }

    /**
     * When a patient is created (without a set doctor), it successfully gets added to the database.
     * Creates the following patient:
     *  - aid       = SET BY DATABASE (should be 2)
     *  - nhsnumber = "0123456789"
     *  - fname     = "Test"
     *  - sname     = "Patient"
     *  - phone     = ""
     *  - doctor    = 0
     */
    @Test
    void addPatient1() {
        assertTrue(accessSQLite.addPatient("0123456789", "Test", "Patient", "", 0));
    }

    /**
     * When a patient is created (with a set doctor), it successfully gets added to the database.
     * Creates the following patient:
     *  - aid       = SET BY DATABASE (should be 2)
     *  - nhsnumber = "0123456789"
     *  - fname     = "Test"
     *  - sname     = "Patient"
     *  - phone     = ""
     *  - doctor    = 1
     */
    @Test
    void addPatient2() {
        assertTrue(accessSQLite.addPatient("0123456789", "Test", "Patient", "", 1));
    }

    /**
     * When a booking is created, it successfully gets added to the database.
     * Creates the following booking:
     *  - bid           = SET BY DATABASE
     *  - startDateTime = "2021-01-01 08:00:00.000"
     *  - endDateTime   = "2021-01-01 08:00:00.000"
     *  - patient       = 1
     *  - doctor        = 1
     */
    @Test
    void addBooking() {
        DateTimeHandler start = new DateTimeHandler(2021, 1, 1, 8, 0);
        DateTimeHandler end = new DateTimeHandler(2021, 1, 1, 9, 0);
        assertTrue(accessSQLite.addBooking(start, end, 1, 1));
    }

    /**
     * When a user has multiple messages on the system they are all returned. The messages should be ordered with
     * the first message in the array being the newest.
     */
    @Test
    void getUserMessages1() {
        ArrayList<String> messages = accessSQLite.getUserMessages("tester1");
        String[] tester1Messages = {"This message is another test.", "This message is a test."};
        assertFalse(messages.isEmpty());
        assertTrue(messages.size() == 2);
        assertArrayEquals(tester1Messages, messages.toArray());
    }

    /**
     * When a user has a message on the system it is returned.
     */
    @Test
    void getUserMessages2() {
        ArrayList<String> messages = accessSQLite.getUserMessages("tester2");
        assertFalse(messages.isEmpty());
        assertTrue(messages.size() == 1);
        assertEquals("This message is the final test.", messages.get(0));
    }

    /**
     * When a user has no messages on the system it they have an empty list.
     */
    @Test
    void getUserMessages3() {
        assertTrue(accessSQLite.getUserMessages("tester4").isEmpty());
    }

    /**
     * Testing getAllPatient() method returns correct values.
     */
    @Test
    void getAllPatients() {
        assertArrayEquals(new String[] {"Test Patient (9876543210)"}, accessSQLite.getAllPatients());
    }

    /**
     * Testing getAllDoctors() method returns correct values.
     */
    @Test
    void getAllDoctors() {
        assertArrayEquals(new String[] {"Test Doc"}, accessSQLite.getAllDoctors());
    }

    /**
     * Testing getAllBookings() method returns correct values.
     */
    @Test
    void getAllBookings() {
        assertNotNull(accessSQLite.getAllBookings());
        assertEquals(1, accessSQLite.getAllBookings().size());
    }

    /**
     * Sending a confirmation message to a patient.
     */
    @Test
    void sendConfirmationMessage1() {
        Patient p = new Patient(1, "", "", "", "", 0);
        assertTrue(accessSQLite.sendConfirmationMessages(p, "This is a test"));
        assertArrayEquals(new String[] {"This is a test"}, accessSQLite.getUserMessages(p));
        assertTrue(accessSQLite.sendConfirmationMessages(p, "This is another test"));
        assertArrayEquals(new String[] {"This is a test", "This is another test"}, accessSQLite.getUserMessages(p));
    }

    /**
     * Sending a confirmation message to a doctor.
     */
    @Test
    void sendConfirmationMessage2() {
        Doctor d = new Doctor(1, "", "", "", "");
        assertTrue(accessSQLite.sendConfirmationMessages(d, "This is a test"));
        assertArrayEquals(new String[] {"This is a test"}, accessSQLite.getUserMessages(d));
        assertTrue(accessSQLite.sendConfirmationMessages(d, "This is another test"));
        assertArrayEquals(new String[] {"This is a test", "This is another test"}, accessSQLite.getUserMessages(d));
    }

    /**
     * Sending a confirmation message to a doctor and patient when booking an appointment.
     */
    @Test
    void sendConfirmationMessage3() {
        Doctor d = new Doctor(1, "", "", "", "");
        Patient p = new Patient(1, "", "", "", "", 0);
        Booking b = new Booking(1, DateTimeHandler.getNow(), DateTimeHandler.getNow(), p, d);
        assertTrue(accessSQLite.sendConfirmationMessages(b, "Message for doctor", "Message for patient"));
        assertArrayEquals(new String[] {"Message for doctor"}, accessSQLite.getUserMessages(b.getDoctor()));
        assertArrayEquals(new String[] {"Message for patient"}, accessSQLite.getUserMessages(b.getPatient()));
    }

    /**
     * After each test is completed, the test database is deleted.
     */
    @AfterEach
    void cleanUp() {
        // Gets the file
        File testingDB = new File("testing.db");
        // Deletes file
        testingDB.delete();
    }
}