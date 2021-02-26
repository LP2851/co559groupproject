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

                // Check to see if the doctor has been given a first name and surname otherwise error
                if (doctorFNameField.getText().strip().isEmpty() || doctorSNameField.getText().strip().isEmpty()) {
                    new DialogBox("Name Error",
                            "The doctor must have a name and a surname.",
                            DialogBox.MessageType.ERROR);
                // Check to see if the number is valid or not inputted otherwise error
                } else if (!validatePhoneNumber(doctorPhoneField.getText().strip())) {
                    new DialogBox("Phone Number Error",
                            "The phone number you have inputted is invalid.\nPhone numbers must be 11 characters in length and only contain numeric characters.",
                            DialogBox.MessageType.ERROR);
                // Succeeded input tests
                } else {
                    // Getting inputted data values
                    String fname, sname, phone, background;
                    fname = doctorFNameField.getText().strip();
                    sname = doctorSNameField.getText().strip();
                    phone = doctorPhoneField.getText().strip();
                    background = doctorBackgroundField.getText().strip();

                    if (accessSQLite.addDoctor(fname, sname, phone, background)) {
                        GlobalUIVars.debug("Returning to Welcome Page");

                        // Dialog box telling you that the doctor has successfully been added
                        new DialogBox("New Doctor Has Been Added to the Database:\n" +
                                        "Name: " + fname + " " + sname + "\n" +
                                        "Phone Number: " + phone + "\n" +
                                        "Background: " + background);

                        // Resetting field on panel
                        doctorFNameField.setText(null);
                        doctorSNameField.setText(null);
                        doctorPhoneField.setText(null);
                        doctorBackgroundField.setText(null);

                        // Change back to welcome screen
                        setActivePanel(welcomePanel);

                    } else {
                        // Show error message if something goes wrong when adding new doctor to the database
                        new DialogBox("Database Error", "An unknown database error occurred.\nPlease try again.", DialogBox.MessageType.ERROR);
                    }


                }
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

    /**
     * Checks to see an inputted phone number is valid.
     * @param phoneNo String containing user input for phone number
     * @return true if number is valid, otherwise false.
     */
    protected boolean validatePhoneNumber(String phoneNo) {
        // Checks characters in phone number are numeric
        try {
            for (char c : phoneNo.toCharArray()) {
                Integer.parseInt("" + c);
            }
        } catch (NumberFormatException e) {
            // Number not valid (contains non-numeric characters)
            return false;
        }

        // If numeric and length of number is 11 characters then number is valid
        if(phoneNo.length() == 11) return true;
        // If number is empty (this is allowed) then number is valid
        else if(phoneNo.isEmpty()) return true;
        // Wrong length then invalid number
        else return false;
    }

}
