package ui;

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

                String username = usernameTextField.getText();
                String password = passwordField1.getText();

                // here check to see if username and hashed password in db
                // TODO Check to see if username and password in database
                if (username.contains("test") && password.contains("test")) {
                    usernameTextField.setText(null);
                    passwordField1.setText(null);

                    GlobalUIVars.debug("Username and password are correct");

                    setActivePanel(welcomePanel);
                    // TODO Get user's name from username and password
                    activeUsersName = "Tester McTesterson";
                    welcomeScreenNameLabel.setText(activeUsersName);
                }

                else {
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
        setTitle(GlobalUIVars.APP_NAME);
        setContentPane(loginPanel);
        loginPanel.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(GlobalUIVars.WINDOW_X, GlobalUIVars.WINDOW_Y);
        setVisible(true);
    }

    /**
     * Changes the displayed panel to the passed panel.
     * @param panel The panel to be displayed.
     */
    private void setActivePanel(JPanel panel) {
        //setTitle(GlobalUIVars.APP_NAME);
        setContentPane(panel);
        //panel.setVisible(true);
        //setDefaultCloseOperation(EXIT_ON_CLOSE);
        //setSize(GlobalUIVars.WINDOW_X, GlobalUIVars.WINDOW_Y);
        //setVisible(true);
        revalidate();
    }
}
