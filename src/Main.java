import mysql.AccessMySQL;
import ui.GUI;

import javax.swing.*;

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

        JFrame frame = new GUI();
        AccessMySQL db = new AccessMySQL();
        db.testConnection();
    }

}
