package ui;

import javax.swing.*;

/**
 * Class to create Dialog boxes easily
 * @author Lucas
 * @version 0.1
 */
public class DialogBox {

    /**
     * Creates a information dialog box with the passed string as its contents.
     * @param message String to be displayed
     */
    public DialogBox(String message) {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame, message);
    }

    /**
     * Creates a new dialog box of the corresponding type.
     * @param title The title of the box (window name).
     * @param message The message to be displayed in the dialog box.
     * @param messageType The type of message the box is for.
     */
    public DialogBox(String title, String message, MessageType messageType) {
        JFrame frame = new JFrame();

        switch (messageType) {
            // Dialog Box for Error Messages
            case ERROR:
                JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
                break;
            // Dialog Box for Information Messages
            case INFORMATION:
                JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
                break;
            // Dialog Box for Warning Messages
            case WARNING:
                JOptionPane.showMessageDialog(frame, message, title, JOptionPane.WARNING_MESSAGE);
                break;
            // Dialog Box for Question Messages
            case QUESTION:
                JOptionPane.showMessageDialog(frame, message, title, JOptionPane.QUESTION_MESSAGE);
                break;
            // Dialog Box for Plain Messages
            case PLAIN:
                JOptionPane.showMessageDialog(frame, message, title, JOptionPane.PLAIN_MESSAGE);
                break;
        }
    }

    public static String createDialogBoxAndGetUserInput(String title, String message) {
        JFrame frame = new JFrame();
        return JOptionPane.showInputDialog(frame, message, title, JOptionPane.PLAIN_MESSAGE);
    }

    /**
     * Enum to specify the type of dialog box wanted.
     */
    public enum MessageType {
        ERROR, INFORMATION, WARNING, QUESTION, PLAIN
    }
}
