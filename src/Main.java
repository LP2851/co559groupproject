import database.AccessSQLite;
import ui.DialogBox;
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
        preLoadDataMaps();
        new GUI();
        if (GlobalUIVars.DEBUG)
            new DialogBox("You are in debug mode.");

    }

    private static void preLoadDataMaps() {
        AccessSQLite accessSQLite = new AccessSQLite();
        accessSQLite.getAllDoctors();
        accessSQLite.getAllPatients();
        accessSQLite.getAllBookings();
    }

}
