package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Deprecated
public class LoginGUI extends JFrame {
    private JLabel titleLabel;
    private JTextField usernameTextField;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JPanel panel;
    private JLabel errorLabel;

    public LoginGUI() {
        initFrame();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlobalUIVars.debug("Button Pressed");

                String username = usernameTextField.getText();
                String password = passwordField1.getText();

                // here check to see if username and hashed password in db
                if (username.contains("test") && password.contains("test")) {
                    usernameTextField.setText(null);
                    passwordField1.setText(null);

                    GlobalUIVars.debug("Username and password are correct");

                    new MainGUI(); // Open the main window
                    dispose();
                }

                else {
                    errorLabel.setVisible(true);
                    GlobalUIVars.debug("Username and password are incorrect.");

                }
            }
        });
    }

    private void initFrame() {
        setTitle(GlobalUIVars.APP_NAME);
        setContentPane(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(GlobalUIVars.WINDOW_X, GlobalUIVars.WINDOW_Y);
        setVisible(true);
    }

}
