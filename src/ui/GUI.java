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
    // Named components that can be in the window.
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
    private JPanel viewBookingsPanel;
    private JButton backFromViewBookingsButton;
    private JTable table1;
    private JButton enterNewBookingButton;
    private JPanel enterNewBookingPanel;
    private JButton goBackToViewBookings;
    private JLabel enterNewBookingLabel;
    private JComboBox patientNameComboBox;
    private JComboBox dayComboBox;
    private JComboBox monthSpinner;
    private JComboBox yearSpinner;
    private JComboBox startHourInput;
    private JComboBox startMinInput;
    private JButton enterBookingButton;
    private JComboBox doctorNameComboBox;
    private JComboBox endHourInput;
    private JComboBox endMinInput;
    private JButton rescheduleBookingButton;

    // Values to store the user's name and also user's username
    private String activeUsersName;
    private String usersUsername;

    // Connection to database
    private AccessSQLite accessSQLite = new AccessSQLite();

    // Message window that opens when then user successfully logs in.
    private MessagesPopup messagesPopup;

    private static String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};

    private static String[] hours = {"07", "08", "09", "10", "11", "12",
                                    "13", "14", "15", "16", "17", "18", "19"};
    private static String[] mins = {"00", "10", "20", "30", "40", "50"};

    /**
     * Constructor for the GUI class. Sets up frame for login screen.
     * Adds action listeners for all buttons in the GUI.
     */
    public GUI() {
        // Loads frame to login screen.
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
                    // Wipes data values from input fields (so next time someone tries to log in, they don't have
                    // to remove old data values).
                    usernameTextField.setText(null);
                    passwordField1.setText(null);

                    // Saves user's username
                    GlobalUIVars.debug("Username and password are correct");
                    usersUsername = username;

                    // Changes panel to welcome screen
                    // Sets person name to the name of the user.
                    setActivePanel(welcomePanel);
                    welcomeScreenNameLabel.setText(activeUsersName);
                    // Opens messages of the user.
                    messagesPopup = new MessagesPopup(username);
                } else {
                    // Shows the error message on the screen
                    errorLabel.setVisible(true);
                    GlobalUIVars.debug("Username and password are incorrect.");
                    // Clears the password field
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
                // Removes details of the current user as they have logged out.
                activeUsersName = "";
                usersUsername = "";
                // If the message popup isn't closed then it will be closed.
                messagesPopup.dispose();

            }
        });

        // Welcome Page- Bookings Button Pressed
        bookingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalUIVars.debug("Opening bookings page");
                setActivePanel(viewBookingsPanel);
            }
        });

        // Welcome Page- Patient Details Button Pressed
        patientDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalUIVars.debug("Opening patient details page");
                // TODO Go to patient details page
            }
        });

        // Welcome Page- Add New Doctor Button Pressed
        addNewDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                GlobalUIVars.debug("Opening new doctor page");
                // Change to enter new doctor window
                setActivePanel(enterNewDocPanel);

            }
        });

        // Welcome Page- Add New Patient Button Pressed
        addNewPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalUIVars.debug("Opening new patient page");
                // TODO Go to new patient page
            }
        });

        // Enter Doctor Page- Go Back Button
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalUIVars.debug("Returning to Welcome Page");

                // Clears details in each of the fields.
                doctorFNameField.setText(null);
                doctorSNameField.setText(null);
                doctorPhoneField.setText(null);
                doctorBackgroundField.setText(null);
                // Change window to welcome screen
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
                        new DialogBox(
                                "Database Error",
                                "An unknown database error occurred.\nPlease try again.",
                                DialogBox.MessageType.ERROR);
                    }


                }
            }
        });

        // View Bookings Page- Go to Enter New Booking Page
        enterNewBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Go to enter new booking page
                // get all patients, get all doctors
                initNewBookingComponents(new String[] {"Patient 1", "Patient 2"}, new String[] {"Doctor 1 (GP)", "Doctor 2"});
                //initNewBookingComponents(accessSQLite.getAllPatients(), accessSQLite.getAllDoctors());
                setActivePanel(enterNewBookingPanel);
            }
        });

        // View Bookings Page- Go Back to Welcome Screen
        backFromViewBookingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setActivePanel(welcomePanel);
            }
        });

        // Enter New Bookings Page- Create New Booking
        enterBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Do checks and then add booking
                // Check booking is valid and doesn't clash with doctor's other appointments
                    // Show doctor's appointments on the day chosen if there is a clash.
                    // And create dialog box.
                // Check booking is valid and doesn't clash with patient's other appointments
                    // Show patient's appointments on the day chosen if there is a clash.
                    // And create dialog box.
                // If succeeds then the booking is added.
            }
        });

        // Enter New Bookings Page- Go Back to View Bookings Screen
        goBackToViewBookings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setActivePanel(viewBookingsPanel);
            }
        });

        // Enter New Bookings Page- When the user selects a patient it should update the doctor field with
        // their usual doctor if they have one.
        patientNameComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Get user details and check for usual doctor
                // String patientNHS = patientNameComboBox.getSelectedItem().toString();
                // getPatientDetails
                // DialogBox showing patient details
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

    /**
     *
     * @param patients
     * @param doctors
     */
    private void initNewBookingComponents(String[] patients, String[] doctors) {
        patientNameComboBox.removeAllItems();
        doctorNameComboBox.removeAllItems();
        dayComboBox.removeAllItems();
        yearSpinner.removeAllItems();
        monthSpinner.removeAllItems();
        startMinInput.removeAllItems();
        startHourInput.removeAllItems();
        endHourInput.removeAllItems();
        endMinInput.removeAllItems();
        for (int i = 1; i < 31; i++) {
            dayComboBox.addItem(i);
        }
        for (int i = 1; i < 6; i++) {
            yearSpinner.addItem(i + 2020);
        }
        for (String m : months) {
            monthSpinner.addItem(m);
        }
        for (String h : hours) {
            startHourInput.addItem(h);
            endHourInput.addItem(h);
        }
        for (String m : mins) {
            startMinInput.addItem(m);
            endMinInput.addItem(m);
        }

        for (String p: patients) {
            patientNameComboBox.addItem(p);
        }

        for (String d : doctors) {
            doctorNameComboBox.addItem(d);
        }
    }

}
