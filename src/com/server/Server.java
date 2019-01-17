package com.server;

import com.common.*;
import com.common.Popup;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashSet;
import java.util.Set;

public class Server extends JFrame implements ConnectionListenerInterface, Runnable {
    private JPanel panel1;
//    private JTextArea textArea1;
    private JTextField textField1;
    private JButton buttonSend;
    private JTable table1;
    private JScrollPane scrollPane;
    boolean listen;
    boolean needToNewConnection = true;

    public Set<ConnectionInterface> getConnections() {
        return connections;
    }

    Set<ConnectionInterface> connections;
    ServerSocket serverSocket;
    Clients clients;
    Banned banned;
    int messageId;
    DefaultTableModel tableModel;

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
    }

    public void setDisableComponents() {
//        textArea1.setEnabled(false);
        textField1.setEnabled(false);
        buttonSend.setEnabled(false);
    }

    public Server(String title) {
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
        setDisableComponents();

//        JTable table1 = new JTable();

        scrollPane = new JScrollPane(table1);
//        scrollPane = new JScrollPane(textArea1);
//        getContentPane().add(scrollPane);
//        panel1.add(scrollPane);


        panel1.add(scrollPane);
//        panel1.add(table1);
//        mainPanel.add(scrollPane);

        tableModel = new DefaultTableModel();
        table1.setModel(tableModel);

        tableModel.addColumn("Column 1");

        for(int i = 0; i < 500; i++)
//            tableModel.insertRow(tableModel.getRowCount(), new Object[] { item });
            tableModel.insertRow(tableModel.getRowCount(), new Object[] { new Message (messageId++, "New", "New very very very very very very very very very very very very very very very very very very very very very long message from server", MessageInterface.CONTENT_TYPE) });

        table1.getColumnModel().getColumn(0).setCellRenderer(new CellRenderer());
        table1.setTableHeader(null);
        table1.setShowGrid(false);
        table1.setRowHeight(25);
        table1.setDragEnabled(false);
        table1.setRowSelectionAllowed(false);
        table1.setCellSelectionEnabled(false);
        table1.setDefaultEditor(Object.class, null);


        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem listenerItem = new JMenuItem("Listener");
        fileMenu.add(listenerItem);
        listenerItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Listener listener = new Listener(Server.this);
                listener.setVisible(true);
            }
        });
        clients = new Clients(Server.this);
        JMenuItem clientsItem = new JMenuItem("Clients");
        fileMenu.add(clientsItem);
        clientsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clients.setVisible(true);
            }
        });
        banned = new Banned(Server.this);
        JMenuItem bannedItem = new JMenuItem("Banned");
        fileMenu.add(bannedItem);
        bannedItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                banned.setVisible(true);
            }
        });
        fileMenu.addSeparator();
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //System.exit(0);
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
                JOptionPane.showMessageDialog(Server.this,
                        "Simple Chat Server\n\n© 2019. All right reserved",
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
                    Message message = new Message(messageId, getNickname(), text, Message.CONTENT_TYPE);

                    for (ConnectionInterface connection : connections) {
                        connection.send(message);
                    }


//                    textArea1.append(getNickname() + ": " + text + "\n");
                    tableModel.insertRow(messageId++, new Object[] { message });
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
                Message message = new Message(messageId, getNickname(), text, Message.CONTENT_TYPE);

                for (ConnectionInterface connection : connections) {
                    connection.send(message);
                }

//                textArea1.append(getNickname() + ": " + text + "\n");
                tableModel.insertRow(messageId++, new Object[] { message });
                textField1.setText("");
            }
        });
        JPanel popupPanel = Popup.createPopupPanel(scrollPane);
        popupPanel.setAlignmentX(0.5f);
        popupPanel.setAlignmentY(0.1f);
//        popupPanel.setBounds(new Rectangle(50, 50, 75, 75));

        JScrollBar verticalScroll = scrollPane.getVerticalScrollBar();
        verticalScroll.addAdjustmentListener(new AdjustmentListener()
        {
            public void adjustmentValueChanged(AdjustmentEvent e)
            {
                if (!e.getValueIsAdjusting()) {

                    if (verticalScroll.getValue() + verticalScroll.getModel().getExtent() * 10 < verticalScroll.getMaximum()) {
                        mainPanel.setFocusable(true);
                        popupPanel.setVisible(true);
                    } else {
                        popupPanel.setVisible(false);
                    }
                }
            }
        });


        mainPanel.add(popupPanel);
        mainPanel.add(panel1);
//        mainPanel.add(scrollPane);
        setVisible(true);

    }

    public void createListener() {
        Listener listener = new Listener(Server.this);
        listener.setVisible(true);
    }

    @Override
    public void connectionCreated(ConnectionInterface connection) {
//        clients.getList1().add()
//        listModel.add(listModel.getSize(), new Item("Nick", "IP", new Socket()));
        clients.getListModel().add(clients.getListModel().getSize(),
                new ClientsDialogItem("Nick",
                        connection.getSocket().getInetAddress().getHostAddress(),
                        connection.getSocket()));
//        System.out.println("New hostname: " + connection.getSocket().getInetAddress().getHostName());
//        System.out.println("New host IP: " + connection.getSocket().getInetAddress().getHostAddress());
        connections.add(connection);

        Message message = new Message(messageId,"", "", Message.GET_NICK_TYPE);
        connection.send(message);

//        System.out.println("Connection was added");
        System.out.println("Send GET_NICK_TYPE");
    }

    @Override
    public synchronized void connectionClosed(ConnectionInterface connection) {
        Message message = new Message(messageId,"", "Get nick type message", Message.CLOSE_TYPE);
        connection.send(message);
        connections.remove(connection);
        System.out.println("Connection was closed");
    }

    @Override
    public synchronized void connectionException(ConnectionInterface connection, Exception e) {
        connectionClosed(connection);
        System.out.println("Exception");
        e.printStackTrace();
    }

    @Override
    public synchronized void receivedContent(ConnectionInterface connection, MessageInterface message) {
        switch(message.getType()) {
            case Message.CONTENT_TYPE:
//                textArea1.append(message.getNick() + ": " + message.getContent() + "\n");
                Message message1 = new Message(messageId, message.getNick(), message.getContent(), message.getType());
                for (ConnectionInterface connection1 : connections) {
                    connection1.send(message1);
                }
                tableModel.insertRow(tableModel.getRowCount(), new Object[] { message1 });
                messageId++;
                break;
            case Message.CLOSE_TYPE:
                ClientsDialogItem item;
                for (int index = 0; index < clients.getListModel().getSize(); index++) {
                    item = (ClientsDialogItem) clients.getListModel().get(index);
                    if (item.getSocket() == connection.getSocket()) {
                        clients.getListModel().remove(index);
                        connectionClosed(connection);
                        break;
                    }
                }
                break;
            case Message.GET_NICK_TYPE:
                connection.setNick(message.getNick());
                for (int index = 0; index < clients.getListModel().getSize(); index++) {
                    item = (ClientsDialogItem) clients.getListModel().get(index);
                    if (item.getSocket() == connection.getSocket()) {
                        clients.getListModel().set(index, new ClientsDialogItem(connection.getNick(),
                                connection.getSocket().getInetAddress().getHostAddress(),
                                connection.getSocket()));

                        break;
                    }
                }
                System.out.println("Received GET_NICK_TYPE: " + connection.getNick());
                break;
            case Message.GET_HISTORY:
                System.out.println("Received GET_HISTORY_TYPE: with " + message.getId() + " id");
                if (message.getId() < 0 || message.getId() > messageId) {
                    System.out.println("Message range of out");
                    return;
                }

                for(int i = 0; i < Message.MESSAGES_HISTORY_COUNT; i++) {
                    if (message.getId() - i < 0)
                        break;
                    message1 = (Message) tableModel.getValueAt(message.getId() - i, 0);
                    System.out.println(message1.getId());
                }
                message1 = new Message(messageId, getNickname(), "End of history", Message.END_HISTORY);
                connection.send(message1);

                break;
        }

    }

    public void start() {
        System.out.println("Server started");
        needToNewConnection = true;
        Thread t = new Thread(this);
        t.setPriority(Thread.MIN_PRIORITY);
        t.start();

    }

    public void stop() {
        try {

            if (connections != null) {
                for (ConnectionInterface connection : connections) {
                    connectionClosed(connection);
                }
            }
            needToNewConnection = false;

//
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server("Simple Chat Server");
        server.setVisible(true);
        //server.start();
    }

    @Override
    public void run() {
        while (needToNewConnection) {
            try {
                Socket socket = serverSocket.accept();
                connectionCreated(new Connection(socket, this));
                Thread.sleep(200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            connections = null;
            serverSocket.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    void exit() {
        System.out.println("Exit");
        stop();
        System.exit(0);
    }
}
