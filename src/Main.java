import database.data.Patient;
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
        new GUI();
        if (GlobalUIVars.DEBUG) {
            new DialogBox("You are in debug mode.");
            new Patient(2851, "0123456789", "Lucas", "Phillips", "01234567890", 1);
        }
    }

}
