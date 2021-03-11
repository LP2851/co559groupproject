import database.AccessSQLite;
import ui.DialogBox;
import ui.GUI;
import ui.GlobalUIVars;

/**
 * The main class for start-up from the command line.
 * @author Lucas
 * @version 0.2
 */
public class Main {

    /**
     * Runs when program starts.
     * @param args
     */
    public static void main(String[] args) {
        // Getting all necessary data from the database.
        preLoadDataMaps();
        new GUI();
        if (GlobalUIVars.DEBUG)
            new DialogBox("You are in debug mode.");

    }

    /**
     * Gets all necessary data from the database.
     * All doctors, patients and booking be added to their corresponding class maps.
     */
    private static void preLoadDataMaps() {
        AccessSQLite accessSQLite = new AccessSQLite();
        accessSQLite.getAllDoctors();
        accessSQLite.getAllPatients();
        accessSQLite.getAllBookings();
    }

}
