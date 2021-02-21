package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGUI extends JFrame {
    private JLabel titleLabel;
    private JTextField usernameTextField;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JPanel panel;


    private static final boolean debug = true ;

    public LoginGUI() {
        setTitle(GlobalUIVars.APP_NAME);
        initFrame();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (debug) System.out.println("Button Pressed");
            }
        });
    }

    private void initFrame() {
        setContentPane(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(GlobalUIVars.WINDOW_X, GlobalUIVars.WINDOW_Y);
        setVisible(true);
    }

}
