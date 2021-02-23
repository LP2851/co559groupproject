package mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class AccessMySQL {
    private Connection connection = null;
    private Statement statement = null;
    private ResultSet resultSet = null;

    // TODO Set up MySQL
    // TODO Add database integration
    // TODO Set up database
    public AccessMySQL() {}

    public void runCommand(String command) {

    }

    public void testConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/co559?user=root&password=co559groupproject");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from test");
            while (resultSet.next()) {
                System.out.print(resultSet.getString("idtest"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
