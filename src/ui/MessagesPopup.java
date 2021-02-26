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
    private JTable table1;

    private AccessSQLite accessSQLite;
    private String username;

    private String[] columnNames = {"Message"};

    public MessagesPopup(String username) {
        this.username = username;
        //showMessages();

        // Get Data ResultSet
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
            initFrame(noMessagePanel);
        } else {
            setMessages(messages);
        }
    }

    /**
     *
     * @param messages
     */
    private void setMessages(ArrayList<String> messages) {

        DefaultTableModel model = (DefaultTableModel) table1.getModel();//new DefaultTableModel();
        model.addColumn("Messages");
        Object[] row = new Object[1];
        for (int i = 0; i < messages.size(); i++) {
            row[0] = messages.get(i);
            model.addRow(row);
        }
        scrollPane.setVisible(true);
        initFrame(messagePanel);

    }

    private void initFrame(JPanel panel) {
        setTitle("Messages"); // setting title of window
        setContentPane(panel); // setting content of window to be the login screen
        panel.setVisible(true); // definitely necessary
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // setting close operation
        setSize(300, 400); // setting frame size
        setVisible(true); // frame is visible
    }
}
