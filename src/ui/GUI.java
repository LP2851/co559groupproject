package ui;

import database.AccessSQLite;
import database.data.AbstractPerson;
import database.data.Booking;
import database.data.Doctor;
import database.data.Patient;
import database.datetime.DateTimeHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * The GUI that is used for the program.
 * @author Lucas
 * @version 0.3
 */
public class GUI extends JFrame {
    // List of months
    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
    // Default text for the patients button
    private static final String defaultPatientDetailsButtonText = "Click to Enter Patient Details";
    private static final String defaultDoctorFilterString = "Click to Select Doctor";
    private static final String defaultPatientFilterString = "Click to Select Patient";
    // Connection to database
    private final AccessSQLite accessSQLite = new AccessSQLite();
    protected AbstractPerson doctorFilter, patientFilter;
    protected boolean filteringDateTime = false;
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
    private JButton nhsNumberButton;
    private JComboBox<Integer> dayComboBox;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;
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
    // Message window that opens when then user successfully logs in.
    private MessagesPopup messagesPopup;

    /**
     * Constructor for the GUI class. Sets up frame for login screen.
     * Adds action listeners for all buttons in the GUI.
     */
    public GUI() {
        // Loads frame to login screen.
        initFrame();
        // Loads action listeners.
        initEventHandlers();
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
        yearComboBox.removeAllItems();
        monthComboBox.removeAllItems();
        startMinInput.removeAllItems();
        startHourInput.removeAllItems();
        endHourInput.removeAllItems();
        endMinInput.removeAllItems();
        dayComboBox.removeAllItems();

        // Adding values to the combo boxes
        for (int i = 1; i < 32; i++)
            dayComboBox.addItem(i);
        for (int i = 1; i < 6; i++)
            yearComboBox.addItem(i + 2020);
        for (String m : MONTHS)
            monthComboBox.addItem(m);

        // Setting the pre-selected date to be the day.
        dayComboBox.setSelectedItem(DateTimeHandler.getNow().getDate());
        monthComboBox.setSelectedIndex(DateTimeHandler.getNow().getMonth());
        yearComboBox.setSelectedItem(DateTimeHandler.getNow().getYear()+1900);

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

    /**
     * Initialises all of the components for the view bookings screen.
     */
    private void initViewBookingComponents() {
        // Clearing the combo boxes
        viewMonthCombo.removeAllItems();
        viewYearCombo.removeAllItems();

        // Adding to the combo boxes
        for (int i = 1; i < 6; i++)
            viewYearCombo.addItem(i + 2020);
        for (String m : MONTHS)
            viewMonthCombo.addItem(m);

        // Setting the text in the doctor/patient filter buttons.
        filterDoctorButton.setText(defaultDoctorFilterString);
        filterPatientButton.setText(defaultPatientFilterString);
        // Turning filters off
        doctorFilter = null;
        patientFilter = null;
        filteringDateTime = false;

        // Updating the table contents
        updateBookingsTable(false);
    }

    /**
     * Updates the bookings table contents.
     * @param filtered Is the table being filtered.
     */
    private void updateBookingsTable(boolean filtered) {
        // Getting the table model data.
        DefaultTableModel model = (DefaultTableModel) bookingsTable.getModel();

        // For the first time.
        if (model.getColumnCount() == 0) {
            model.addColumn("Date");
            model.addColumn("Start Time");
            model.addColumn("End Time");
            model.addColumn("Patient");
            model.addColumn("Doctor");
        }

        // Getting all of the bookings from the database.
        Booking[] allBookings = accessSQLite.getAllBookings().toArray(new Booking[0]);

        // Clearing data already in table.
        model.setRowCount(0);

        // Adding bookings to table
        for(Booking b : allBookings) {
            // Adds to the table if not filtering
            // Or it is filtering and the booking fits the filter requirements.
            if (!filtered || passesFilter(b)) {
                String date = b.getStartDateTime().toString().split(" ")[0];
                String start = b.getStartDateTime().toString().split(" ")[1];
                String end = b.getEndDateTime().toString().split(" ")[1];

                // Adding data to table.
                model.addRow(new String[] {
                        date,
                        start,
                        end,
                        b.getPatient().toString(),
                        b.getDoctor().toString()
                });
            }
        }
    }

    /**
     * Returns if the booking passed fits the filter the user has selected.
     * @param b The booking to be tested.
     * @return True if the booking meets the filter requirements, otherwise false.
     */
    protected boolean passesFilter(Booking b) {
        // Is the patient filter has been set and the patient is the same
        if (patientFilter != null && b.getPatient().getId() != patientFilter.getId())
            return false;
        // Is the doctor filter has been set and the doctor is the same
        else if (doctorFilter != null && b.getDoctor().getId() != doctorFilter.getId())
            return false;
        // Is the date/time being used as a filter and is the month the same
        else if (filteringDateTime && b.getStartDateTime().getDate().getMonth() != viewMonthCombo.getSelectedIndex())
            return false;
        // Is the date/time being used as a filter and is the year the same
        else if (filteringDateTime && b.getStartDateTime().getDate().getYear() + 1900 != (int) viewYearCombo.getSelectedItem())
            return false;
        // If successfully avoids failing any of the above then the booking passes the filter
        else
            return true;
    }

    /*
    ALL OF THE BUTTON PRESS FUNCTIONS
     */

    /**
     * Method is called in the init of the GUI object.
     */
    private void initEventHandlers() {
        // Login Page- Login Button Pressed
        loginButton.addActionListener(e -> pressedLoginButton());

        // Welcome Page- Logout Button Pressed
        logoutButton.addActionListener(e -> pressedLogoutButton());

        // Welcome Page- Bookings Button Pressed
        bookingsButton.addActionListener(e -> {
            GlobalUIVars.debug("Opening bookings page");
            setActivePanel(viewBookingsPanel);
            initViewBookingComponents();
        });

        // Welcome Page- Add New Doctor Button Pressed
        addNewDoctorButton.addActionListener(e -> {
            GlobalUIVars.debug("Opening new doctor page");
            // Change to enter new doctor window
            setActivePanel(enterNewDocPanel);
        });

        // Enter Doctor Page- Go Back Button
        goBackButton.addActionListener(e -> pressedGoBackButton(welcomePanel));

        // Enter Doctor Page- Submit New Doctor Button
        enterNewDoctorButton.addActionListener(e -> pressedEnterNewDoctorButton());

        // View Bookings Page- Go to Enter New Booking Page
        enterNewBookingButton.addActionListener(e -> {
            initNewBookingComponents(accessSQLite.getAllDoctors());
            setActivePanel(enterNewBookingPanel);
        });

        // View Bookings Page- Go Back to Welcome Screen
        backFromViewBookingsButton.addActionListener(e -> setActivePanel(welcomePanel));

        // Enter New Bookings Page- Go Back to View Bookings Screen
        goBackToViewBookings.addActionListener(e -> pressedGoBackButton(viewBookingsPanel));

        // Enter New Bookings Page- NHS Number Button
        // Opens dialog box to get information
        nhsNumberButton.addActionListener(e -> pressedNHSNumberButton());

        // Enter New Bookings Page- Start Time Hour Changes the End Hour too.
        startHourInput.addActionListener(e -> endHourInput.setSelectedItem(startHourInput.getSelectedItem()));

        // Enter New Bookings Page- Start Time Minute Changes the End Minute too.
        startMinInput.addActionListener(e -> endMinInput.setSelectedItem(startMinInput.getSelectedItem()));

        // Enter New Bookings Page- Enter the booking
        enterBookingButton.addActionListener(e -> pressedEnterNewBookingButton());

        // View Bookings Page- Filter By Doctor Button
        filterDoctorButton.addActionListener(e -> pressedFilterByDoctorButton());

        // View Bookings Page- Filter by Patient Button
        filterPatientButton.addActionListener(e -> pressedFilterByPatientButton());

        // View Bookings- Search Button
        // Filters the table
        searchButton.addActionListener(e -> updateBookingsTable(true));

        // View Bookings- Clear Button
        clearSearchButton.addActionListener(e -> initViewBookingComponents());

        // View Bookings- Month Filter
        viewMonthCombo.addActionListener(e -> {
            // Makes sure the date and time are being used as a filter.
            filteringDateTime = true;
            // Stopping errors
            if (viewYearCombo.getSelectedItem() == null) {
                viewYearCombo.setSelectedItem(2021);
            }
        });

        // View Bookings- Year Filter
        viewYearCombo.addActionListener(e -> {
            // Makes sure the date and time are being used as a filter.
            filteringDateTime = true;
            // Stopping errors
            if (viewMonthCombo.getSelectedItem() == null) {
                viewMonthCombo.setSelectedItem("JAN");
            }
        });
    }

    /**
     * Method that is called when the login button is pressed.
     */
    private void pressedLoginButton() {
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

    /**
     * Method that is called when the logout button is pressed
     */
    private void pressedLogoutButton() {
        GlobalUIVars.debug("Logging out from the account of: " + welcomeScreenNameLabel.getText());
        setActivePanel(loginPanel);
        // Removes details of the current user as they have logged out.
        activeUsersName = "";
        usersUsername = "";
        // If the message popup isn't closed then it will be closed.
        messagesPopup.dispose();
    }

    /**
     * Method that is called when a go back button is pressed
     * @param toPanel The panel to go back to.
     */
    private void pressedGoBackButton(JPanel toPanel) {
        if (toPanel.equals(welcomePanel)) {
            GlobalUIVars.debug("Returning to Welcome Page");
            // Clears details in each of the fields.
            doctorFNameField.setText(null);
            doctorSNameField.setText(null);
            doctorPhoneField.setText(null);
            doctorBackgroundField.setText(null);
            // Change window to welcome screen
            setActivePanel(welcomePanel);
        } else if (toPanel.equals(viewBookingsPanel)) {
            nhsNumberButton.setText(defaultPatientDetailsButtonText);
            setActivePanel(viewBookingsPanel);
            initViewBookingComponents();
        }
    }

    /**
     * Method that is called when the enter new doctor button is pressed.
     */
    private void pressedEnterNewDoctorButton() {
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

                // Resetting. Getting all data from the database.
                Doctor.resetMap();
                // Getting doctor added with list.
                Doctor[] allDoctors = Doctor.getAllDoctors();
                // Constructing message.
                String message =
                        DateTimeHandler.getNow().toString() +
                                " :: You have been added to the system!";
                accessSQLite.sendConfirmationMessages(allDoctors[allDoctors.length-1], message);

            } else {
                // Show error message if something goes wrong when adding new doctor to the database
                new DialogBox(
                        "Database Error",
                        "An unknown database error occurred.\nPlease try again.",
                        DialogBox.MessageType.ERROR);
            }
        }
    }

    /**
     * Method that is called when the NHS number button is pressed on the enter new booking screen.
     */
    private void pressedNHSNumberButton() {
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
                nhsNumberButton.setText(defaultPatientDetailsButtonText);
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
                        nhsNumberButton.setText(p.toString());
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

    /**
     * Method that is called when the enter new booking button is pressed.
     */
    private void pressedEnterNewBookingButton() {
        int day = (int) dayComboBox.getSelectedItem();
        int month = monthComboBox.getSelectedIndex();
        int year = (int) yearComboBox.getSelectedItem();

        // Creating DateTimeHandlers for the start and end points inputted
        DateTimeHandler startDTH = getDateTimeHandlerFor(day, month, year,
                (int) startHourInput.getSelectedItem(),
                (int) (startMinInput.getSelectedItem().equals("00") ? 0 : startMinInput.getSelectedItem()));
        DateTimeHandler endDTH = getDateTimeHandlerFor(day, month, year,
                (int) endHourInput.getSelectedItem(),
                (int) (endMinInput.getSelectedItem().equals("00") ? 0 : endMinInput.getSelectedItem()));

        // If the patient hasn't been set then error
        if (nhsNumberButton.getText().equals(defaultPatientDetailsButtonText)) {
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
            monthComboBox.setSelectedIndex(DateTimeHandler.getNow().getMonth());
            yearComboBox.setSelectedItem(DateTimeHandler.getNow().getYear() + 1900);
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
            String nhsnumber = getNHSNumberFromPatientString(nhsNumberButton.getText());
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
                        nhsNumberButton.setText(defaultPatientDetailsButtonText);
                        setActivePanel(viewBookingsPanel);
                        initViewBookingComponents();

                        // Resetting. Getting all data from database.
                        Booking.resetMap();
                        // Getting doctor added with list.
                        // Constructing messages for doctor and patient.
                        String messageDoctor = String.format(
                                "%s :: You have a new booking for %s until %s with %s.",
                                DateTimeHandler.getNow().toString(),
                                startDTH.toString(),
                                endDTH.toString(),
                                b.getPatient().toString());

                        String messagePatient = String.format(
                                "%s :: You have a new booking for %s until %s with %s. " +
                                        "If this is incorrect please contact us.",
                                DateTimeHandler.getNow().toString(),
                                startDTH.toString(),
                                endDTH.toString(),
                                b.getDoctor().toString());
                        // Sending messages.
                        accessSQLite.sendConfirmationMessages(b, messageDoctor, messagePatient);

                    } else {
                        // Failed to add to database
                        new DialogBox("Database Error", "An unknown database error occurred.\nPlease try again.", DialogBox.MessageType.ERROR);
                    }
                    break;
            }
        }
    }

    /**
     * Method that is called when the filter by doctor button is pressed.
     */
    private void pressedFilterByDoctorButton() {
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

    /**
     * Method that is called when the filter by patient button is pressed.
     */
    private void pressedFilterByPatientButton() {
        // Getting the patient chosen by the user
        patientFilter = (Patient) DialogBox.createDialogBoxAndGetUserInput(
                "Filter By Patient",
                "Filtering by the patient:\nPress the Cancel button to clear selection.",
                Patient.getAllPatients(),
                (filterPatientButton.getText().equals(defaultPatientFilterString)) ? null : filterPatientButton.getText()
        );
        // If patient not chosen then set text to default (clearing the patient part of the filter)
        if(patientFilter != null)
            filterPatientButton.setText(patientFilter.toString());
        // Setting the text of the patient button to be the chosen patient.
        else
            filterPatientButton.setText(defaultPatientFilterString);
    }
}
