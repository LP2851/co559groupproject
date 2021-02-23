package ui;

import javax.swing.*;

@Deprecated
public class MainGUI extends JFrame {

    private JPanel panel;

    public MainGUI() {
        initFrame();
    }

    public void initFrame() {
        setTitle(GlobalUIVars.APP_NAME);
        setContentPane(panel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(GlobalUIVars.WINDOW_X, GlobalUIVars.WINDOW_Y);
        setVisible(true);
    }
}
