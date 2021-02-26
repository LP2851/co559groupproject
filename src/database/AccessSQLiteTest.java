package database;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AccessSQLiteTest {

    private AccessSQLite accessSQLite;

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
     * When a doctor is created, it successfully get added to the database.
     * Creates the following doctor:
     *  - aid       = SET BY DATABASE (should be 1)
     *  - fname     = "Doctor"
     *  - sname     = "Test"
     *  - phone     = "01234567890"
     *  - background= "GP"
     */
    @Test
    void addDoctor() {
        assertTrue(accessSQLite.addDoctor("Doctor", "Test", "01234567890", "GP"));
    }
    /*
    @Test
    void addPatient() {
    }

    @Test
    void addBooking() {
    }

    @Test
    void viewBookings() {
    }

    @Test
    void updateBooking() {
    }

    @Test
    void updatePatient() {
    }

    @Test
    void removeBooking() {
    }
    */

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

    @AfterEach
    void cleanUp() {
        File testingDB = new File("testing.db");
        testingDB.delete();
    }

}