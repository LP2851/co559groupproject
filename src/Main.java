import mysql.AccessMySQL;
import ui.GUI;
import ui.GlobalUIVars;

/**
 * The main class for start-up from the command line.
 * @author Lucas
 * @version 0.1.1
 */
public class Main {


    /**
     * Runs when program starts.
     * @param args
     */
    public static void main(String[] args) {
        new GUI();
        AccessMySQL db = new AccessMySQL();
        if (GlobalUIVars.DEBUG) db.testConnection();
        //System.out.println(db.checkUsernamePassword("test", "123"));
    }

}
