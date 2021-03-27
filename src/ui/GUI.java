package ui;

import database.AccessSQLite;
import database.data.AbstractPerson;
import database.data.Booking;
import database.data.Doctor;
import database.data.Patient;
import database.datetime.DateTimeHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The GUI that is used for the program.
 * @author Lucas
 * @version 0.2
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
    private JTable bookingsTable;
    private JButton enterNewBookingButton;
    private JPanel enterNewBookingPanel;
    private JButton goBackToViewBookings;
    private JLabel enterNewBookingLabel;
    private JButton patientNameComboBox;
    private JComboBox<Integer> dayComboBox;
    private JComboBox<String> monthSpinner;
    private JComboBox<Integer> yearSpinner;
    private JComboBox<Integer> startHourInput;
    private JComboBox<String> startMinInput;
    private JButton enterBookingButton;
    private JComboBox<String> doctorNameComboBox;
    private JComboBox<Integer> endHourInput;
    private JComboBox<String> endMinInput;
    private JButton rescheduleBookingButton;
    private JButton filterDoctorButton;
    private JButton filterPatientButton;
    private JComboBox<String> viewMonthCombo;
    private JButton searchButton;
    private JButton clearSearchButton;
    private JComboBox<Integer> viewYearCombo;

    // Values to store the user's name and also user's username
    private String activeUsersName;
    private String usersUsername;

    // Connection to database
    private final AccessSQLite accessSQLite = new AccessSQLite();

    // Message window that opens when then user successfully logs in.
    private MessagesPopup messagesPopup;

    // List of months
    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};

    // Default text for the patients button
    private static final String defaultPatientDetailsButtonText = "Click to Enter Patient Details";

    private static AbstractPerson doctorFilter, patientFilter;
    private static boolean filteringDateTime = false;
    private static final String defaultDoctorFilterString = "Click to Select Doctor";
    private static final String defaultPatientFilterString = "Click to Select Patient";

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
                initViewBookingComponents();
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
                initNewBookingComponents(accessSQLite.getAllDoctors());
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

        // Enter New Bookings Page- Go Back to View Bookings Screen
        goBackToViewBookings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                patientNameComboBox.setText(defaultPatientDetailsButtonText);
                setActivePanel(viewBookingsPanel);
                initViewBookingComponents();
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

        // Enter New Bookings Page- NHS Number Button
        // Opens dialog box to get information
        patientNameComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Is the inputted value a valid NHS number (or nothing)
                boolean isValidValue = false;
                // The users input
                String result;

                while(!isValidValue) {
                    // Gets an input from user
                    result = DialogBox.createDialogBoxAndGetUserInput(
                            "Patient NHS Number Lookup",
                            "Please Enter the 10 digit NHS Number of the Patient:\nIf you want to exit this screen then do not enter anything in the text field and press ok.")
                            .strip();
                    // If empty, then resets the button and leaves the Dialog box loop.
                    if (result == null) {
                        // Do nothing
                        if (GlobalUIVars.DEBUG)
                            System.out.println("Stopped entering patient details.");
                    } else if (result.isEmpty()) {
                        patientNameComboBox.setText(defaultPatientDetailsButtonText);
                        isValidValue = true;
                    } else {
                        // If the NHS number is correct (numeric and 10 chars long)
                        if (validateNHSNumber(result)) {
                            // Gets the patient from the number provided
                            Patient p = isNHSNumber(result);
                            // If the patient exists in the system then show patient details, leave loop, update button contents
                            if (p != null) {
                                new DialogBox("Patient: " + result,
                                        p.getAllDetailsString() + "\nIf these values are not correct then try again.",
                                        DialogBox.MessageType.INFORMATION);
                                patientNameComboBox.setText(p.toString());
                                // If the patient has a doctor specified then the doctor field is changed
                                if (p.getDoctor() != null)
                                    doctorNameComboBox.setSelectedItem(p.getDoctor().toString());
                                isValidValue = true;
                            } else {
                                // When inputting number that doesn't exist in the system then dialog box and loop
                                new DialogBox("Patient Doesn't Exist",
                                        "The patient NHS number doesn't exist.\nYou will be returned to the input screen.\nIf you wish to stop inputting a new patient then dont type in the text field.",
                                        DialogBox.MessageType.ERROR);
                            }
                        } else {
                            // If the NHS number is invalid then dialog box and loop
                            new DialogBox("NHS Number Error",
                                    "The NHS number you have inputted is invalid.\nNHS numbers must be 10 characters in length and only contain numeric characters.",
                                    DialogBox.MessageType.ERROR);
                        }
                    }
                }
            }
        });

        // Enter New Bookings Page- Start Time Hour Changes the End Hour too.
        startHourInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endHourInput.setSelectedItem(startHourInput.getSelectedItem());
            }
        });

        // Enter New Bookings Page- Start Time Minute Changes the End Minute too.
        startMinInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endMinInput.setSelectedItem(startMinInput.getSelectedItem());
            }
        });

        // Enter New Bookings Page- Enter the booking
        enterBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int day = (int) dayComboBox.getSelectedItem();
                int month = monthSpinner.getSelectedIndex();
                int year = (int) yearSpinner.getSelectedItem();

                // Creating DateTimeHandlers for the start and end points inputted
                DateTimeHandler startDTH = getDateTimeHandlerFor(day, month, year,
                        (int) startHourInput.getSelectedItem(),
                        (int) (startMinInput.getSelectedItem().equals("00") ? 0 : startMinInput.getSelectedItem()));
                DateTimeHandler endDTH = getDateTimeHandlerFor(day, month, year,
                        (int) endHourInput.getSelectedItem(),
                        (int) (endMinInput.getSelectedItem().equals("00") ? 0 : endMinInput.getSelectedItem()));

                // If the patient hasn't been set then error
                if (patientNameComboBox.getText().equals(defaultPatientDetailsButtonText)) {
                    new DialogBox("Patient Not Selected",
                            "You have not selected a patient.\nPlease enter a patient and try again.",
                            DialogBox.MessageType.ERROR);
                // If the inputted date is not after the time of entry then error
                } else if (!startDTH.checkDateIsAfterNow()) {
                    new DialogBox("Date Error",
                            "The date inputted is before today.\nThe date will now be reset to today.",
                            DialogBox.MessageType.ERROR);
                    // Changes date input box to the today.
                    dayComboBox.setSelectedItem(DateTimeHandler.getNow().getDate());
                    monthSpinner.setSelectedIndex(DateTimeHandler.getNow().getMonth());
                    yearSpinner.setSelectedItem(DateTimeHandler.getNow().getYear() + 1900);
                // If the ending time is set before the starting time then error
                } else if (!startDTH.checkIsBefore(endDTH)) {
                    new DialogBox("Time Error",
                            "The end time inputted is not after the start time.",
                            DialogBox.MessageType.ERROR);
                    // Not sure if I want to do this vvv
                    // endHourInput.setSelectedItem(startHourInput.getSelectedItem());
                    // endMinInput.setSelectedItem(startMinInput.getSelectedItem());
                // If the date is not valid (31st Feb) then error
                } else if (!DateTimeHandler.isValidDateFromInputs(day, month, year)){
                    new DialogBox("Invalid Date Error",
                            "The date chosen is not valid.",
                            DialogBox.MessageType.ERROR);
                } else {
                    // Get the patient details
                    String nhsnumber = getNHSNumberFromPatientString(patientNameComboBox.getText());
                    Patient p = Patient.getPatientFromNHSNumber(nhsnumber);

                    // Create booking object based on inputs
                    Booking b = new Booking(startDTH, endDTH, p, Doctor.getDoctorFromString((String) doctorNameComboBox.getSelectedItem()));

                    // Checks there are no booking clashes
                    switch (Booking.authenticateBooking(b)) {
                        // There is a clash with the doctor's schedule
                        case DOCTOR_CLASH:
                            // TODO EXTRA: Show user the doctors bookings.
                            new DialogBox("Booking Clash Error: Doctor",
                                    "The booking clashes with the chosen doctor's bookings.\nPlease choose another doctor or another date/time.",
                                    DialogBox.MessageType.ERROR);
                            break;
                        // There is a clash with the patient's schedule
                        case PATIENT_CLASH:
                            // TODO EXTRA: Show user the clients bookings.
                            new DialogBox("Booking Clash Error: Patient",
                                    "The booking clashes with the chosen patient's bookings.\nPlease choose another date/time.",
                                    DialogBox.MessageType.ERROR);
                            break;
                        // There are no clashes
                        case AUTHORISED:
                            // Tries to add to database
                            if(accessSQLite.addBooking(startDTH, endDTH, b.getDoctor().getId(), b.getPatient().getId())) {
                                // Successfully added to database
                                new DialogBox("New Booking Created:\n" + b.toString());
                                patientNameComboBox.setText(defaultPatientDetailsButtonText);
                                setActivePanel(viewBookingsPanel);
                                initViewBookingComponents();
                            } else {
                                // Failed to add to database
                                new DialogBox("Database Error", "An unknown database error occurred.\nPlease try again.", DialogBox.MessageType.ERROR);
                            }
                            break;
                    }
                }
            }
        });

        // View Bookings Page- Filter By Doctor Button
        filterDoctorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doctorFilter = (Doctor) DialogBox.createDialogBoxAndGetUserInput(
                        "Filter By Doctor",
                        "Filtering by the doctor:\nPress the Cancel button to clear selection.",
                        Doctor.getAllDoctors(),
                        (filterDoctorButton.getText().equals(defaultDoctorFilterString)) ? null : filterDoctorButton.getText()
                );
                if(doctorFilter != null)
                    filterDoctorButton.setText(doctorFilter.toString());
                else
                    filterDoctorButton.setText(defaultDoctorFilterString);
            }
        });

        // View Bookings Page- Filter by Patient Button
        filterPatientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                patientFilter = (Patient) DialogBox.createDialogBoxAndGetUserInput(
                        "Filter By Patient",
                        "Filtering by the patient:\nPress the Cancel button to clear selection.",
                        Patient.getAllPatients(),
                        (filterPatientButton.getText().equals(defaultPatientFilterString)) ? null : filterPatientButton.getText()
                );
                if(patientFilter != null)
                    filterPatientButton.setText(patientFilter.toString());
                else
                    filterPatientButton.setText(defaultPatientFilterString);
            }
        });

        // View Bookings- Search Button
        searchButton.addActionListener(e -> updateBookingsTable(true));

        // View Bookings- Clear Button
        clearSearchButton.addActionListener(e -> initViewBookingComponents());
        viewMonthCombo.addActionListener(e -> {
            filteringDateTime = true;
            if (viewYearCombo.getSelectedItem() == null) {
                viewYearCombo.setSelectedItem(2021);
            }
        });
        viewYearCombo.addActionListener(e -> {
            filteringDateTime = true;
            if (viewMonthCombo.getSelectedItem() == null) {
                viewMonthCombo.setSelectedItem("JAN");
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
        // Wrong length then invalid number
        if(phoneNo.length() == 11) return true;
        // If number is empty (this is allowed) then number is valid
        else return phoneNo.isEmpty();
    }

    /**
     * Checks to see an inputted NHS number is valid.
     * @param nhsNumber String containing user input for NHS number
     * @return true if number is valid, otherwise false.
     */
    protected boolean validateNHSNumber(String nhsNumber) {
        // Checks characters in phone number are numeric
        try {
            for (char c : nhsNumber.toCharArray()) {
                Integer.parseInt("" + c);
            }
        } catch (NumberFormatException e) {
            // Number not valid (contains non-numeric characters)
            return false;
        }

        // If numeric and length of number is 10 characters then number is valid
        // Wrong length then invalid number
        if(nhsNumber.length() == 10) return true;
        // If number is empty (this is allowed) then number is valid
        else return nhsNumber.isEmpty();
    }

    /**
     * Returns a patient based on the passed NHS number.
     * If patient doesn't exist then returns null.
     * @param nhsNumber A validated NHS number
     * @return A patient based on the passed NHS number, or null.
     */
    protected Patient isNHSNumber(String nhsNumber) {
        return Patient.getPatientFromNHSNumber(nhsNumber);
    }

    /**
     * Extracts the patient's NHS number from a patient string
     * Firstname Surname (NHS_NUMBER)
     * @param patientString String containing an NHS number in brackets
     * @return NHS number from the patient string
     */
    private String getNHSNumberFromPatientString(String patientString) {
        int startIndex = patientString.indexOf("(") + 1;
        return patientString.substring(startIndex, startIndex+10);
    }

    /**
     * Creates a new DateTimeHandler object for the passed values
     * @param day The day
     * @param month The month
     * @param year The year
     * @param hour The hour
     * @param min The minute
     * @return A DateTimeHandler for the passed values.
     */
    private DateTimeHandler getDateTimeHandlerFor(int day, int month, int year, int hour, int min) {
        return new DateTimeHandler(year, month, day, hour, min);
    }

    /**
     * Initialises all some input components on the enter new booking screen
     * @param doctors List of doctor name strings
     */
    private void initNewBookingComponents(String[] doctors) {
        // Clears items from input components
        doctorNameComboBox.removeAllItems();
        yearSpinner.removeAllItems();
        monthSpinner.removeAllItems();
        startMinInput.removeAllItems();
        startHourInput.removeAllItems();
        endHourInput.removeAllItems();
        endMinInput.removeAllItems();
        dayComboBox.removeAllItems();


        // Adding values to the combo boxes

        for (int i = 1; i < 32; i++)
            dayComboBox.addItem(i);
        for (int i = 1; i < 6; i++)
            yearSpinner.addItem(i + 2020);

        for (String m : MONTHS)
            monthSpinner.addItem(m);


        // Setting the pre-selected date to be the day.
        dayComboBox.setSelectedItem(DateTimeHandler.getNow().getDate());
        monthSpinner.setSelectedIndex(DateTimeHandler.getNow().getMonth());
        yearSpinner.setSelectedItem(DateTimeHandler.getNow().getYear()+1900);

        for (int h = 7; h < 20; h++) {
            startHourInput.addItem(h);
            endHourInput.addItem(h);
        }
        for (int m = 0; m < 60; m += 10) {
            startMinInput.addItem((m == 0) ? "00" :"" + m);
            endMinInput.addItem((m == 0) ? "00" : "" + m);
        }

        for (String d : doctors)
            doctorNameComboBox.addItem(d);

    }

    private void initViewBookingComponents() {
        viewMonthCombo.removeAllItems();
        viewYearCombo.removeAllItems();

        for (int i = 1; i < 6; i++)
            viewYearCombo.addItem(i + 2020);

        for (String m : MONTHS)
            viewMonthCombo.addItem(m);

        filterDoctorButton.setText(defaultDoctorFilterString);
        filterPatientButton.setText(defaultPatientFilterString);
        doctorFilter = null;
        patientFilter = null;
        filteringDateTime = false;

        updateBookingsTable(false);
    }

    private void updateBookingsTable(boolean filtered) {
        DefaultTableModel model = (DefaultTableModel) bookingsTable.getModel();

        if (model.getColumnCount() == 0) {
            model.addColumn("Date");
            model.addColumn("Start Time");
            model.addColumn("End Time");
            model.addColumn("Patient");
            model.addColumn("Doctor");
        }

        Booking[] allBookings = accessSQLite.getAllBookings().toArray(new Booking[0]);

        model.setRowCount(0);
        model.fireTableDataChanged();

        for(Booking b : allBookings) {
            if (!filtered || passesFilter(b)) {
                String date = b.getStartDateTime().toString().split(" ")[0];
                String start = b.getStartDateTime().toString().split(" ")[1];
                String end = b.getEndDateTime().toString().split(" ")[1];

                model.addRow(new Object[] {
                        date,
                        start,
                        end,
                        b.getPatient(),
                        b.getDoctor()
                });
            }
        }
        model.fireTableDataChanged();
    }

    protected boolean passesFilter(Booking b) {
        if (patientFilter != null && b.getPatient().getId() != patientFilter.getId()) {
            return false;
        } else if (doctorFilter != null && b.getDoctor().getId() != doctorFilter.getId()) {
            return false;
        } else if (filteringDateTime && b.getStartDateTime().getDate().getMonth() != viewMonthCombo.getSelectedIndex()) {
            return false;
        } else if (filteringDateTime && b.getStartDateTime().getDate().getYear() + 1900 != (int) viewYearCombo.getSelectedItem()){
            return false;
        }
        return true;
    }



}
