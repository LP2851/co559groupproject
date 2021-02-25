package ui;

import database.AccessSQLite;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The GUI that is used for the program.
 * @author Lucas
 * @version 0.1
 */
public class GUI extends JFrame {
    private JPanel framePanel;
    private JPanel loginPanel;
    private JTextField usernameTextField;
    private JPasswordField passwordField1;
    private JLabel errorLabel;
    private JButton loginButton;
    private JPanel welcomePanel;
    private JButton bookingsButton;
    private JButton patientDetailsButton;
    private JButton addNewDoctorButton;
    private JButton addNewPatientButton;
    private JButton logoutButton;
    private JLabel welcomeScreenNameLabel;
    private String activeUsersName;

    private AccessSQLite accessSQLite = new AccessSQLite();

    /**
     * Constructor for the GUI class. Sets up frame for login screen.
     * Adds action listeners for all buttons in the GUI.
     */
    public GUI() {
        initFrame();

        // Login Page- Login Button Pressed
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalUIVars.debug("Button Pressed");

                String username = usernameTextField.getText().strip();
                String password = passwordField1.getText().strip();

                // TODO Password hashing?
                activeUsersName = accessSQLite.checkUsernamePassword(username, password);
                if (!activeUsersName.equals("")) { // username/password exists
                    usernameTextField.setText(null);
                    passwordField1.setText(null);

                    GlobalUIVars.debug("Username and password are correct");

                    setActivePanel(welcomePanel);
                    welcomeScreenNameLabel.setText(activeUsersName);
                } else {
                    errorLabel.setVisible(true);
                    GlobalUIVars.debug("Username and password are incorrect.");
                    passwordField1.setText(null);
                }
            }
        });

        // Welcome Page- Logout Button Pressed
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalUIVars.debug("Logging out from the account of: " + welcomeScreenNameLabel.getText());
                setActivePanel(loginPanel);
                activeUsersName = "";

            }
        });

        // Welcome Page- Bookings Button Pressed
        bookingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalUIVars.debug("Opening bookings page");
            }
        });

        // Welcome Page- Patient Details Button Pressed
        patientDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalUIVars.debug("Opening patient details page");
            }
        });

        // Welcome Page- Add New Doctor Button Pressed
        addNewDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalUIVars.debug("Opening new doctor page");
            }
        });

        // Welcome Page- Add New Patient Button Pressed
        addNewPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalUIVars.debug("Opening new patient page");
            }
        });
    }

    /**
     * Initialising the frame with the variables required for a good layout.
     * Sets the frame to display the login panel.
     */
    private void initFrame() {
        setTitle(GlobalUIVars.APP_NAME); // setting title of window
        setContentPane(loginPanel); // setting content of window to be the login screen
        loginPanel.setVisible(true); // definitely necessary
        setDefaultCloseOperation(EXIT_ON_CLOSE); // setting close operation
        setSize(GlobalUIVars.WINDOW_X, GlobalUIVars.WINDOW_Y); // setting frame size
        setVisible(true); // frame is visible
    }

    /**
     * Changes the displayed panel to the passed panel.
     * @param panel The panel to be displayed.
     */
    private void setActivePanel(JPanel panel) {
        setContentPane(panel); // changing the panel to the provided panel
        revalidate(); // redraws window
    }
}
