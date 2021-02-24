package mysql;

import ui.GlobalUIVars;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AccessMySQL {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    private static String connectionURL = "jdbc:mysql://localhost/co559?user=root&password=co559groupproject";

    // TODO Set up MySQL
    // TODO Add database integration
    // TODO Set up database

    // TODO Make database accessible from any device ??

    /**
     * Empty constructor for the AccessMySQL class
     */
    public AccessMySQL() {}

    /**
     * Connects to database and runs the command it is passed, saving result in private field resultSet.
     * @param command String for the command to be run
     * @throws Exception Error if cannot connect to database, or database throws an error
     */
    protected void runCommand(String command) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(connectionURL);
        statement = connection.createStatement();
        resultSet = statement.executeQuery(command);

    }

    /**
     * This is test code to check the database is set up.
     */
    public void testConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(connectionURL);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from test");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("idtest"));
            }
            GlobalUIVars.debug("Connected to Database Successfully");
        } catch (Exception e) {
            e.printStackTrace();
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
        String sqlCommand = "select fname, sname from administrator where username = '" + username + "' and password = '" + password +"';";

        //resultSet.getFetchSize();
        try {
            runCommand(sqlCommand);
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
     * TODO Generate and run add doctor command ( INSERT INTO (...) VALUES (...); )
     */
    public void addDoctor() {

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
    private void logAccess() {}


    /**
     * Returns result set (previously run query)
     * @return Result set of previously run query
     */
    public ResultSet getResultSet() {
        return resultSet;
    }

}
