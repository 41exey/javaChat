package com.client;

import com.common.*;
import com.common.Popup;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;

public class Client extends JFrame implements ConnectionListenerInterface {
    private JPanel panel1;
//    private JTextArea textArea1;
    private JTextField textField1;
    private JButton buttonSend;
    private JTable table1;
    private JScrollPane scrollPane;
    boolean connect;
    ConnectionInterface connection;
    DefaultTableModel tableModel;
    boolean getHistory;

    public void clearTextArea() {
//        textArea1.setText("");
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getNickname() {
        return Nickname;
    }

    private String Nickname = "Nickname";

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    private int port = Connection.PORT;

    public void setEnableComponents() {
//        textArea1.setEnabled(true);
        textField1.setEnabled(true);
        buttonSend.setEnabled(true);
        scrollPane.getViewport().getView().setEnabled(true);
        scrollPane.createVerticalScrollBar().setEnabled(true);
        scrollPane.getHorizontalScrollBar().setEnabled(true);
        table1.setEnabled(true);
    }

    public void setDisableComponents() {
//        textArea1.setEnabled(false);
        textField1.setEnabled(false);
        buttonSend.setEnabled(false);
        scrollPane.getViewport().getView().setEnabled(false);
        scrollPane.createVerticalScrollBar().setEnabled(false);
        scrollPane.getHorizontalScrollBar().setEnabled(false);
        table1.setEnabled(false);
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getIP() {
        return IP;
    }

    private String IP = Connection.IP;

    Client(String title) {
        super(title);

        JPanel mainPanel = new JPanel(){
            @Override
            public boolean isOptimizedDrawingEnabled() {
                return false;
            }
        };
        mainPanel.setLayout(new OverlayLayout(mainPanel));
        setContentPane(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setPreferredSize(new Dimension(400, 450));
        setResizable(false);
//        setContentPane(panel1);


//        scrollPane = new JScrollPane(textArea1);
        scrollPane = new JScrollPane(table1);
        panel1.add(scrollPane);

        setDisableComponents();

        tableModel = new DefaultTableModel();
        table1.setModel(tableModel);

        tableModel.addColumn("Column 1");
        table1.getColumnModel().getColumn(0).setCellRenderer(new CellRenderer());

        /*
        for(int i = 0; i < 500; i++)
//            tableModel.insertRow(tableModel.getRowCount(), new Object[] { item });
            tableModel.insertRow(tableModel.getRowCount(), new Object[] { new Message (tableModel.getRowCount() + 15, "New", "New very very very very very very very very very very very very very very very very very very very very very long message", MessageInterface.CONTENT_TYPE) });
            */


        table1.setTableHeader(null);
        table1.setShowGrid(false);
        table1.setRowHeight(25);
        table1.setDragEnabled(false);
        table1.setRowSelectionAllowed(false);
        table1.setCellSelectionEnabled(false);
        table1.setDefaultEditor(Object.class, null);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem listenerItem = new JMenuItem("Connector");
        fileMenu.add(listenerItem);
        listenerItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Connector connector = new Connector(Client.this);
                connector.setVisible(true);

            }
        });
        fileMenu.addSeparator();
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.exit(0);
                exit();
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        JMenu aboutMenu = new JMenu("About");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutMenu.add(aboutItem);
        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Client.this,
                        "Simple Chat Client\n\n© 2019. All right reserved",
                        "About",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);
        setJMenuBar(menuBar);
        pack();
        setLocationByPlatform(true);
        setLocationRelativeTo(null);
//        textArea1.setEditable(false);
        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!textField1.getText().isEmpty()) {
                    String text = textField1.getText().trim();
                    System.out.println(text);
                    Message message = new Message(0, getNickname(), text, Message.CONTENT_TYPE);
                    connection.send(message);
                    //textArea1.append(getNickname() + ": " + textField1.getText() + "\n");
                    textField1.setText("");
                }
            }
        });
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() != KeyEvent.VK_ENTER)
                    return;

                if (textField1.getText().isEmpty())
                    return;

                String text = textField1.getText().trim();
                System.out.println(text);
                Message message = new Message(0, getNickname(), text, Message.CONTENT_TYPE);
                connection.send(message);
                textField1.setText("");
            }
        });
        JPanel popupPanel = Popup.createPopupPanel(scrollPane);
//        popupPanel.setBounds(new Rectangle(50, 50, 75, 75));

        JScrollBar verticalScroll = scrollPane.getVerticalScrollBar();
        verticalScroll.addAdjustmentListener(new AdjustmentListener()
        {
            public void adjustmentValueChanged(AdjustmentEvent e)
            {
                if (!e.getValueIsAdjusting()) {

                    if (!verticalScroll.isVisible())
                        return;

                    if (verticalScroll.getValue() == 0 && connect && !getHistory) {


                        System.out.println("GET_HISTORY");

                        Message firstMessage = (Message) tableModel.getValueAt(0, 0);

                        if (firstMessage.getId() == 0)
                            System.out.println("First message is received: ");

                        else {
                            setDisableComponents();
                            getHistory = true;
                            System.out.println("GET_HISTORY: Start id: " + firstMessage.getId());
                            Message message = new Message(firstMessage.getId(), getNickname(), "Get me history", Message.GET_HISTORY);
                            connection.send(message);
                        }

                        verticalScroll.setValue(1);

                    }

                    if (verticalScroll.getValue() + verticalScroll.getModel().getExtent() * Message.DOWN_BUTTON_FACTOR < verticalScroll.getMaximum()) {
                        mainPanel.setFocusable(true);
                        popupPanel.setVisible(true);
                    } else {
                        popupPanel.setVisible(false);
                    }
                }
            }
        });

        mainPanel.add(popupPanel);
        getContentPane().add(panel1);
        setVisible(true);
    }

    public void createConnector() {
        Connector connector = new Connector(Client.this);
        connector.setVisible(true);
    }

    @Override
    public void connectionCreated(ConnectionInterface connection) {
        System.out.println("Client connection was created");
    }

    @Override
    public void connectionClosed(ConnectionInterface connection) {
        Message message = new Message(0, "", "", Message.CLOSE_TYPE);
        connection.send(message);
        connection.close();
        System.out.println("Client connection was closed");
    }

    @Override
    public void connectionException(ConnectionInterface connection, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void receivedContent(ConnectionInterface connection, MessageInterface message) {
//        textArea1.append(message.toString());
        switch (message.getType()) {
            case Message.CONTENT_TYPE:
                tableModel.insertRow(tableModel.getRowCount(), new Object[] { message });
                JScrollBar scroll = scrollPane.getVerticalScrollBar();

                if (getNickname().equals(message.getNick())) {
                    Rectangle cell = table1.getCellRect(tableModel.getRowCount() - 1, 0, true);
                    table1.scrollRectToVisible(cell);
                }

                if (scroll.getValue() > (scroll.getMaximum() - scroll.getModel().getExtent() * 2)) {
                    Rectangle cell = table1.getCellRect(tableModel.getRowCount() - 1, 0, true);
                    table1.scrollRectToVisible(cell);
                }

//                textArea1.append(message.getNick() + ": " + message.getContent() + "\n");
                break;
            case Message.CLOSE_TYPE:
                connection.close();
                setDisableComponents();
                connect = false;
                JOptionPane.showMessageDialog(this,
                        "Server closed connection",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                break;
            case Message.GET_NICK_TYPE:
                Message message1 = new Message(0, getNickname(), "Get nick type message", Message.GET_NICK_TYPE);
                connection.send(message1);
                System.out.println("Received GET_NICK_TYPE and Send GET_NICK_TYPE");

                System.out.println("GET_HISTORY after GET_NICK_TYPE: Start id: -1");
                message1 = new Message(-1, getNickname(), "Get me history", Message.GET_HISTORY);
                connection.send(message1);
                break;
            case Message.GET_HISTORY:
                tableModel.insertRow(0, new Object[] { message });
                break;
            case Message.END_HISTORY:
                setEnableComponents();
                getHistory = false;
                break;
        }

//        textArea1.append("\n");
    }

    public static void main(String[] args) {
        Client client = new Client("Simple Chat Client");
        client.setVisible(true);
//        Client client2 = new Client("Simple Chat Client");
//        client2.setVisible(true);
    }

    void exit() {
        System.out.println("Exit");
        connectionClosed(connection);
        System.exit(0);
    }
}
