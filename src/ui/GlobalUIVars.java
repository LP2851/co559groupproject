package ui;

public class GlobalUIVars {
    public static final int WINDOW_X = 600;
    public static final int WINDOW_Y = 400;

    public static final String APP_NAME = "Administrator Doctor Program Thingy";

    public static final boolean DEBUG = true;

    public static void debug(String s) {
        if (DEBUG) {
            System.out.println("[Debug] :: " + s);
        }
    }


}
