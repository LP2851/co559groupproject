package database;

import java.sql.*;

/**
 *
 */
public class AccessSQLite {
    // Variables used for database access
    private Connection connection = null;
    private ResultSet resultSet = null;
    private PreparedStatement preparedStatement = null;

    // URL used to connect to the database file.
    private static String connectionURL = "jdbc:sqlite:co559.db";

    // SQL Statements
    public static final String NEW_DOCTOR = "insert into doctor (fname, sname, phone, background) values (?, ?, ?, ?) ;";
    public static final String CHECK_USERNAME_PASSWORD = "select fname, sname from administrator where username = ? and password = ?;";

    /**
     * Empty constructor for the AccessMySQL class
     */
    public AccessSQLite() {}

    /**
     * This is test code to check the database is set up.
     */
    public void testConnection() {

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(connectionURL);
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
        // PreparedStatement - prevents sql injection
        try {
            connection = DriverManager.getConnection(connectionURL);
            preparedStatement = connection.prepareStatement(CHECK_USERNAME_PASSWORD);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();



            if(resultSet.next()) {
                return resultSet.getString("fname") + " " + resultSet.getString("sname");
            }
            else return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
            connection = DriverManager.getConnection(connectionURL);
            preparedStatement = connection.prepareStatement(NEW_DOCTOR);

            preparedStatement.setString(1, fname);
            preparedStatement.setString(2, sname);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, background);

            preparedStatement.executeUpdate();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * TODO Generate and run add patient command ( INSERT INTO (...) VALUES (...); )
     */
    public void addPatient() {

    }

    /**
     * TODO Generate and run add booking command ( INSERT INTO (...) VALUES (...); )
     */
    public void addBooking() {

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
     * @throws SQLException Can happen when the query is executed.
     */
    public ResultSet getUserMessages(String username) throws SQLException {
        connection = DriverManager.getConnection(connectionURL);

        // Cannot use prepared statement here as broke some of the syntax
        Statement statement = connection.createStatement();
        resultSet  = statement.executeQuery("select m.message " + // getting the messages
                                                "from messages m, administrator a " + // using these tables
                                                "where m.aid = a.aid " + // this is a join one aids
                                                "and a.username = '" + username + "' " + // getting messages for the user
                                                "order by mid desc;" ); // the values at the top are newest
        return resultSet;
    }


    /**
     * Returns result set (previously run query)
     * @return Result set of previously run query
     */
    public ResultSet getResultSet() {
        return resultSet;
    }


}
