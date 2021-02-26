package ui;

import javax.swing.*;

public class DialogBox {

    public DialogBox(String message) {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame, message);
    }


    public DialogBox(String title, String message, MessageType warningMessage) {
        JFrame frame = new JFrame();
        switch (warningMessage) {
            case ERROR:
                JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
                break;
            case INFORMATION:
                JOptionPane.showMessageDialog(frame, message, title, JOptionPane.INFORMATION_MESSAGE);
                break;
            case WARNING:
                JOptionPane.showMessageDialog(frame, message, title, JOptionPane.WARNING_MESSAGE);
                break;
            case QUESTION:
                JOptionPane.showMessageDialog(frame, message, title, JOptionPane.QUESTION_MESSAGE);
                break;
            case PLAIN:
                JOptionPane.showMessageDialog(frame, message, title, JOptionPane.PLAIN_MESSAGE);
                break;
        }
    }

    public enum MessageType {
        ERROR, INFORMATION, WARNING, QUESTION, PLAIN
    }
}
