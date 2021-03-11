package database;

import database.data.AbstractPerson;
import database.data.Booking;
import database.data.Doctor;
import database.data.Patient;
import database.datetime.DateTimeHandler;

import java.sql.*;
import java.util.ArrayList;


/**
 * Class used to connect to and get data from the SQLite database.
 * @author Lucas
 * @version 0.1
 */
public class AccessSQLite {
    // Variables used for database access
    private ResultSet resultSet = null;
    private PreparedStatement preparedStatement = null;

    // URL used to connect to the database file.
    private static String connectionURL = "jdbc:sqlite:co559.db";

    // SQL Statements
    private static final String NEW_DOCTOR = "insert into doctor (fname, sname, phone, background) values (?, ?, ?, ?) ;";
    private static final String CHECK_USERNAME_PASSWORD = "select fname, sname from administrator where username = ? and password = ?;";
    private static final String NEW_BOOKING = "insert into booking (start, end, doctor, patient) values (?, ?, ?, ?);";
    private static final String NEW_PATIENT_WITH_DOCTOR = "insert into patient (nhsnumber, fname, sname, phone, doctor) values (?, ?, ?, ?, ?);";
    private static final String NEW_PATIENT_WITHOUT_DOCTOR = "insert into patient (nhsnumber, fname, sname, phone) values (?, ?, ?, ?);";
    private static final String ALL_PATIENTS = "select * from patient;";
    private static final String ALL_DOCTORS = "select * from doctor;";
    private static final String DOCTOR_FROM_PATIENT = "select doctor.fname, doctor.sname from doctor, patient where patient.nhsnumber = ? and patient.doctor = doctor.did;";
    private static final String ALL_BOOKINGS = "select * from booking;";

    /**
     * Empty constructor for the AccessMySQL class
     */
    public AccessSQLite() {}

    /**
     * Constructor only used when creating and querying a test database.
     * @param databaseFilename The file name of the testing database.
     */
    public AccessSQLite(String databaseFilename) {
        connectionURL = "jdbc:sqlite:" + databaseFilename;
    }

    /**
     * Runs a command that doesn't have a response. These are UPDATE, INSERT and CREATE statements.
     * @param sql An UPDATE, INSERT or CREATE statement.
     */
    public void runUpdateCommand(String sql) {
        try {
            // Connects to the database
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionURL);
            // Creates and runs query
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            // Closes connection
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This is test code to check the database is set up and that its can be connected to.
     */
    public void testConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionURL);
            connection.close();
            System.out.println("IT WORKS");
        } catch (Exception e) {
            System.out.println("IT BROKE");
        }
    }

    /**
     * Constructs and runs SQL command on database to get the first name and surname of the user
     * valid usernames and passwords are entered.
     * Creating this command:
     *      select fname, sname
     *      from administrator
     *      where username = "INSERT HERE"
     *          and password = "INSERT HERE";
     * @param username Username entered by the user
     * @param password Password entered by the user
     * @return The name of the user. (FirstName Surname), or empty string if user not found.
     */
    public String checkUsernamePassword(String username, String password) {
        String name = "";
        // PreparedStatement - prevents sql injection
        try {
            // Opening connection
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionURL);
            preparedStatement = connection.prepareStatement(CHECK_USERNAME_PASSWORD);
            // Adding values to statement- prevents SQL injection
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                name =  resultSet.getString("fname") + " " + resultSet.getString("sname");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    /**
     * Adds a new doctor to the database
     * @param fname First name of the doctor
     * @param sname Surname of the doctor
     * @param phone Phone number (string) of the doctor
     * @param background Background details of the doctor
     */
    public boolean addDoctor(String fname, String sname, String phone, String background) {
        try {
            // Opening connection
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionURL);
            preparedStatement = connection.prepareStatement(NEW_DOCTOR);
            // Adding values to statement- prevents SQL injection
            preparedStatement.setString(1, fname);
            preparedStatement.setString(2, sname);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, background);

            preparedStatement.executeUpdate();

            connection.close();
            Doctor.resetMap();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * TODO Generate and run add patient command ( INSERT INTO (...) VALUES (...); )
     */
    public boolean addPatient(String nhsNumber, String fname, String sname, String phone, int doctor) {
        // Needs Patient.resetMap();
        try {
            // Opening connection
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionURL);
            preparedStatement = connection.prepareStatement((doctor != -1) ? NEW_PATIENT_WITH_DOCTOR : NEW_PATIENT_WITHOUT_DOCTOR);
            // Adding values to statement- prevents SQL injection
            preparedStatement.setString(1, nhsNumber);
            preparedStatement.setString(2, fname);
            preparedStatement.setString(3, sname);
            preparedStatement.setString(4, phone);
            if (doctor != -1)
                preparedStatement.setInt(5, doctor);

            preparedStatement.executeUpdate();
            connection.close();
            Patient.resetMap();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * TODO Generate and run add booking command ( INSERT INTO (...) VALUES (...); )
     */
    public boolean addBooking(DateTimeHandler start, DateTimeHandler end, int doctorID, int patientID) {
        try {
            // Opening connection
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionURL);
            preparedStatement = connection.prepareStatement(NEW_BOOKING);
            // Adding values to statement- prevents SQL injection
            preparedStatement.setString(1, start.toString());
            preparedStatement.setString(2, end.toString());
            preparedStatement.setInt(3, doctorID);
            preparedStatement.setInt(4 , patientID);

            preparedStatement.executeUpdate();

            connection.close();
            Booking.resetMap();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * TODO Generate and run view bookings command ( SELECT ... FROM booking WHERE ... SORT BY ...)
     */
    public void viewBookings() {

    }

    /**
     * TODO Generate and run update booking command
     */
    public void updateBooking() {

    }

    /**
     * TODO Generate and run update patient details command
     */
    public void updatePatient() {

    }

    /**
     * TODO Generate and run delete booking command
     */
    public void removeBooking() {

    }

    /**
     * TODO Some form of log that can be checked.
     */
    private void logAccess(String username) {}

    /**
     * Returns the results from asking the database for all of the messages for the user.
     * @param username The user's username- to make they only get messages for them
     * @return The results from asking the database for all of the messages for the user.
     */
    public ArrayList<String> getUserMessages(String username)  {
        // List to store all user messages as strings
        ArrayList<String> messages = new ArrayList<>();
        try {
            // Opening connection
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionURL);

            // Cannot use prepared statement here as broke some of the syntax
            Statement statement = connection.createStatement();
            resultSet  = statement.executeQuery("select m.message " + // getting the messages
                    "from messages m, administrator a " + // using these tables
                    "where m.aid = a.aid " + // this is a join one aids
                    "and a.username = '" + username + "' " + // getting messages for the user
                    "order by mid desc;" ); // the values at the top are newest

            // Moving returned messages into arraylist to be returned
            while(resultSet.next()) {
                messages.add(resultSet.getString("message"));
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    public String[] getAllPatients() {
        ArrayList<AbstractPerson> patients = new ArrayList<>();
        try {
            // Opening connection
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionURL);

            // Cannot use prepared statement here as broke some of the syntax
            preparedStatement = connection.prepareStatement(ALL_PATIENTS);

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                patients.add(new Patient(
                        resultSet.getInt("pid"),
                        resultSet.getString("nhsnumber"),
                        resultSet.getString("fname"),
                        resultSet.getString("sname"),
                        resultSet.getString("phone"),
                        resultSet.getInt("doctor")
                ));
            }

            connection.close();

            //Collections.sort(patients);

            return AbstractPerson.toArray(patients);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[] {};
    }

    public String[] getAllDoctors() {
        ArrayList<AbstractPerson> doctors = new ArrayList<>();
        try {
            // Opening connection
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionURL);

            // Cannot use prepared statement here as broke some of the syntax
            preparedStatement = connection.prepareStatement(ALL_DOCTORS);

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                doctors.add(new Doctor(
                        resultSet.getInt("did"),
                        resultSet.getString("fname"),
                        resultSet.getString("sname"),
                        resultSet.getString("phone"),
                        resultSet.getString("background")
                ));
            }

            connection.close();
            // Collections.sort(AbstractPerson.toArray(doctors)); // sorted in toArray method
            return AbstractPerson.toArray(doctors);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[] {};
    }

    public ArrayList<Booking> getAllBookings() {
        ArrayList<Booking> bookings = new ArrayList<>();
        try {
            // Opening connection
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionURL);

            // Cannot use prepared statement here as broke some of the syntax
            preparedStatement = connection.prepareStatement(ALL_BOOKINGS);

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                bookings.add(new Booking(
                    resultSet.getInt("bid"),
                    resultSet.getString("startDateTime"),
                    resultSet.getString("endDateTime"),
                    resultSet.getInt("patient"),
                    resultSet.getInt("doctor")
                ));
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }

}
