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
 * @version 0.3
 * @version 0.3
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
    private static final String NEW_BOOKING = "insert into booking (startDateTime, endDateTime, doctor, patient) values (?, ?, ?, ?);";
    private static final String NEW_PATIENT_WITH_DOCTOR = "insert into patient (nhsnumber, fname, sname, phone, doctor) values (?, ?, ?, ?, ?);";
    private static final String NEW_PATIENT_WITHOUT_DOCTOR = "insert into patient (nhsnumber, fname, sname, phone) values (?, ?, ?, ?);";
    private static final String ALL_PATIENTS = "select * from patient;";
    private static final String ALL_DOCTORS = "select * from doctor;";
    private static final String ALL_BOOKINGS = "select * from booking;";

    private static final String SEND_CONF_MESSAGE_PATIENT = "insert into messagesPatient (pid, message) values (?, ?);";
    private static final String SEND_CONF_MESSAGE_DOCTOR = "insert into messagesDoctor (did, message) values (?, ?);";

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
     * Adds a new patient to the database
     * @param nhsNumber Patient's NHS number
     * @param fname Patient's first name
     * @param sname Patient's surname
     * @param phone Patient's phone number
     * @param doctor Patient's doctor (can be 0 if not set)
     * @return If the process succeeded
     */
    public boolean addPatient(String nhsNumber, String fname, String sname, String phone, int doctor) {
        try {
            // Opening connection
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionURL);
            // Changes SQL statement based on if doctor is set
            preparedStatement = connection.prepareStatement((doctor != 0) ? NEW_PATIENT_WITH_DOCTOR : NEW_PATIENT_WITHOUT_DOCTOR);
            // Adding values to statement- prevents SQL injection
            preparedStatement.setString(1, nhsNumber);
            preparedStatement.setString(2, fname);
            preparedStatement.setString(3, sname);
            preparedStatement.setString(4, phone);

            // Runs when doctor is set
            if (doctor != 0)
                preparedStatement.setInt(5, doctor);

            preparedStatement.executeUpdate();
            connection.close();
            // Refreshing maps
            Patient.resetMap();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Adds a new booking to the database
     * @param start Start time object
     * @param end End time object
     * @param doctorID Doctor's id
     * @param patientID Patient's id
     * @return If the addition to the database was a success
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

    public String[] getUserMessages(AbstractPerson p) {
        // List to store all user messages as strings
        ArrayList<String> messages = new ArrayList<>();
        try {
            // Opening connection
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionURL);

            // Cannot use prepared statement here as broke some of the syntax
            Statement statement = connection.createStatement();

            if (p instanceof Patient)
                resultSet = statement.executeQuery("select message from messagesPatient where pid = " + p.getId() +";");
            else
                resultSet = statement.executeQuery("select message from messagesDoctor where did = " + p.getId() +";");

            // Moving returned messages into arraylist to be returned
            while(resultSet.next()) {
                messages.add(resultSet.getString("message"));
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages.toArray(new String[0]);
    }

    /**
     * Returns an array of patient names.
     * @return An array of patient names from the database
     */
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

    /**
     * Returns an array of all doctors that are in database as string with names and backgrounds
     * @return An array of all doctors that are in the database as strings with names and backgrounds
     */
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

    /**
     * Returns an ArrayList containing all of the bookings in the database
     * @return An ArrayList containing all of the bookings in the database
     */
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

    /**
     * Sends confirmation message to the the person that is passed.
     * @param person The person the message is for.
     * @param message The message to be sent to the person.
     * @return True if the message was sent successfully, false otherwise.
     */
    public boolean sendConfirmationMessages(AbstractPerson person, String message) {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection(connectionURL);

            preparedStatement = connection.prepareStatement((person instanceof Doctor) ? SEND_CONF_MESSAGE_DOCTOR: SEND_CONF_MESSAGE_PATIENT);

            preparedStatement.setInt(1, person.getId());
            preparedStatement.setString(2, message);

            preparedStatement.executeUpdate();

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Sends confirmation messages to the people involved in the booking.
     * Returns true if the messages were sent, otherwise returns false.
     * @param booking The booking that the confirmation message is about.
     * @param messageDoctor The message to be sent to the doctor.
     * @param messagePatient The message to be sent to the patient.
     * @return True if the messages were sent successfully, otherwise false.
     */
    public boolean sendConfirmationMessages(Booking booking, String messageDoctor, String messagePatient) {
        return sendConfirmationMessages(booking.getDoctor(), messageDoctor) && sendConfirmationMessages(booking.getPatient(), messagePatient);
    }


}
