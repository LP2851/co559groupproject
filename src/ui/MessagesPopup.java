package ui;

import database.AccessSQLite;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Creates a message popup when the user signs in.
 * @author Lucas
 * @version 0.1
 */
public class MessagesPopup extends JFrame {

    // Card panel storing the two options for panels
    private JPanel panel1;
    // Stores the table, that can
    private JScrollPane scrollPane;
    // Displayed when the user has no messages
    private JPanel noMessagePanel;
    // Displayed when the user has messages
    private JPanel messagePanel;
    // The table showing the messages to the user.
    private JTable table1;

    // The access point to the database
    private AccessSQLite accessSQLite = new AccessSQLite();
    // The username of the user signed in
    private String username;

    /**
     * Constructor for a MessagePopup object.
     * @param username The username of the user- so that we can get all of their messages.
     */
    public MessagesPopup(String username) {
        this.username = username;
        // Creating the frame.
        showMessages();
    }

    /**
     * Returns the user's messages from the database
     * @return The user's messages from the database
     */
    private ArrayList<String> getMessages() {
        // Creating an empty ArrayList
        ArrayList<String> messages = new ArrayList<>();
        try {
            // Attempting to get the messages from the database
            ResultSet rs = accessSQLite.getUserMessages(username);

            // Adding messages to the list.
            while(rs.next()) {
                messages.add(rs.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            // ArrayList will be empty
        }
        return messages;
    }

    /**
     * Main method for choosing between a frame telling the user one of two things
     *  1. The messages they have if they have any
     *  2. The fact that they have no messages
     */
    private void showMessages() {
        // Getting the messages
        ArrayList<String> messages = getMessages();
        // If no messages then...
        if (messages.size() == 0) {
            initFrame(noMessagePanel);
        } else {
            setMessages(messages);
        }
    }

    /**
     * Method to put all of the messages into the table and then display the frame.
     * @param messages List of messages the user has.
     */
    private void setMessages(ArrayList<String> messages) {
        // Getting the model of the table
        DefaultTableModel model = (DefaultTableModel) table1.getModel();//new DefaultTableModel();
        // Creating a column.
        model.addColumn("Messages");
        // Rows need to be passed to the table as strings.
        String[] row = new String[1];
        // For each message do add it as a row to the table.
        for (int i = 0; i < messages.size(); i++) {
            row[0] = messages.get(i);
            model.addRow(row);
        }
        // Creating the frame
        initFrame(messagePanel);

    }

    /**
     * Method that creates the frame with the panel containing all contents.
     * @param panel The panel to be displayed in the frame.
     */
    private void initFrame(JPanel panel) {
        setTitle("Messages"); // setting title of window
        setContentPane(panel); // setting content of window to be the login screen
        panel.setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // setting close operation
        setSize(300, 400); // setting frame size
        setVisible(true); // frame is visible
    }
}
