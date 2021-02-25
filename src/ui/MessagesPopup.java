package ui;

import database.AccessSQLite;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.util.ArrayList;

public class MessagesPopup extends JFrame {

    private JPanel panel1;
    private JScrollPane scrollPane;
    private JPanel noMessagePanel;
    private JPanel messagePanel;

    private AccessSQLite accessSQLite;
    private String username;

    private String[] columnNames = {"Message"};

    public MessagesPopup(String username) {
        this.username = username;
        initFrame();

        // Get Data ResultSet
    }

    private void initFrame() {

    }

    private ArrayList<String> getMessages() {
        ArrayList<String> messages = new ArrayList<>();
        try {
            ResultSet rs = accessSQLite.getUserMessages(username);

            while(rs.next()) {
                messages.add(rs.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    private void showMessages() {
        ArrayList<String> messages = getMessages();

        if (messages.size() > 0) {
            setNoMessages();
        } else {
            setMessages(messages);
        }
    }

    private void setNoMessages() {
        setTitle("Messages"); // setting title of window
        setContentPane(noMessagePanel); // setting content of window to be the login screen
        noMessagePanel.setVisible(true); // definitely necessary
        setDefaultCloseOperation(EXIT_ON_CLOSE); // setting close operation
        setSize(GlobalUIVars.WINDOW_X, GlobalUIVars.WINDOW_Y); // setting frame size
        setVisible(true); // frame is visible
    }

    /**
     *
     * @param messages
     */
    private void setMessages(ArrayList<String> messages) {
        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Messages");
        for (int i = 0; i < messages.size(); i++) {
            model.addRow(new Object[] {messages.get(i)});
        }


        table.setModel(model);

    }
}
