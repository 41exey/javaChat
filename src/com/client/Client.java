package com.client;

import com.common.*;
import com.common.Popup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.Socket;

public class Client extends JFrame implements ConnectionListenerInterface {
    private JPanel panel1;
    private JTextArea textArea1;
    private JTextField textField1;
    private JButton buttonSend;
    private JScrollPane scrollPane;
    boolean connect;
    ConnectionInterface connection;

    public void clearTextArea() {
        textArea1.setText("");
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
        textArea1.setEnabled(true);
        textField1.setEnabled(true);
        buttonSend.setEnabled(true);
    }

    public void setDisableComponents() {
        textArea1.setEnabled(false);
        textField1.setEnabled(false);
        buttonSend.setEnabled(false);
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

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new OverlayLayout(mainPanel));
        setContentPane(mainPanel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setPreferredSize(new Dimension(400, 450));
        setResizable(false);
//        setContentPane(panel1);
        setDisableComponents();

        scrollPane = new JScrollPane(textArea1);
        panel1.add(scrollPane);
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
                System.exit(0);
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
        textArea1.setEditable(false);
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
        popupPanel.setAlignmentX(0.5f);
        popupPanel.setAlignmentY(0.1f);
//        popupPanel.setBounds(new Rectangle(50, 50, 75, 75));

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
                textArea1.append(message.getNick() + ": " + message.getContent() + "\n");
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

}
