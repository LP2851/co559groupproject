package ui;

/**
 * Class for Global variables.
 * @author Lucas
 * @version 0.1
 */
// @Deprecated
public class GlobalUIVars {
    // Main Window Dimensions
    public static final int WINDOW_X = 720;
    public static final int WINDOW_Y = 480;

    // Name of the main window
    public static final String APP_NAME = "Administrator Doctor Program Thingy";

    // Debug value
    public static final boolean DEBUG = false;

    /**
     * If debug mode is on, this method will print debug messages in the terminal.
     * @param s Message to be printed in the terminal.
     */
    public static void debug(String s) {
        if (DEBUG) {
            System.out.println("[Debug] :: " + s);
        }
    }

}
