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
    private JPanel enterNewDocPanel;
    private JButton goBackButton;
    private JButton enterNewDoctorButton;
    private JTextField doctorFNameField;
    private JTextField doctorSNameField;
    private JTextField doctorPhoneField;
    private JTextField doctorBackgroundField;

    private String activeUsersName;
    private String usersUsername;

    private AccessSQLite accessSQLite = new AccessSQLite();

    private MessagesPopup messagesPopup;

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
                    usersUsername = username;

                    setActivePanel(welcomePanel);
                    welcomeScreenNameLabel.setText(activeUsersName);

                    messagesPopup = new MessagesPopup(username);
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
                usersUsername = "";
                messagesPopup.dispose();

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

                setActivePanel(enterNewDocPanel);

            }
        });

        // Welcome Page- Add New Patient Button Pressed
        addNewPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalUIVars.debug("Opening new patient page");
            }
        });

        // Enter Doctor Page- Go Back Button
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalUIVars.debug("Returning to Welcome Page");

                doctorFNameField.setText(null);
                doctorSNameField.setText(null);
                doctorPhoneField.setText(null);
                doctorBackgroundField.setText(null);

                setActivePanel(welcomePanel);
            }
        });

        // Enter Doctor Page- Submit New Doctor Button
        enterNewDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalUIVars.debug("Attempting to submit doctor data");
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
        panel.setVisible(true);
        revalidate(); // redraws window
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
