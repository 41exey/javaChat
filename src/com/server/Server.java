package com.server;

import com.common.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashSet;
import java.util.Set;

public class Server extends JFrame implements ConnectionListenerInterface, Runnable {
    JPanel panel1;
    private JTextArea textArea1;
    private JTextField textField1;
    private JButton buttonSend;
    private JScrollPane scrollPane;
    boolean listen;
    boolean needToNewConnection = true;

    public Set<ConnectionInterface> getConnections() {
        return connections;
    }

    Set<ConnectionInterface> connections;
    ServerSocket serverSocket;
    Clients clients;
    boolean hostname = true;

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

    public Server(String title) {
        super(title);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setPreferredSize(new Dimension(400, 450));
        setResizable(false);
        setContentPane(panel1);
        setDisableComponents();

        scrollPane = new JScrollPane(textArea1);
        getContentPane().add(scrollPane);
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
        textArea1.setEditable(false);
        buttonSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 if (!textField1.getText().isEmpty()) {
                    String text = textField1.getText().trim();
                    System.out.println(text);
                    Message message = new Message(getNickname(), text, Message.CONTENT_TYPE);

                    for (ConnectionInterface connection : connections) {
                        connection.send(message);
                    }

                    textArea1.append(getNickname() + ": " + text + "\n");
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
                Message message = new Message(getNickname(), text, Message.CONTENT_TYPE);

                for (ConnectionInterface connection : connections) {
                    connection.send(message);
                }

                textArea1.append(getNickname() + ": " + text + "\n");
                textField1.setText("");
            }
        });
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
                new Item("Nick",
                        connection.getSocket().getInetAddress().getHostAddress(),
                        connection.getSocket()));
//        System.out.println("New hostname: " + connection.getSocket().getInetAddress().getHostName());
//        System.out.println("New host IP: " + connection.getSocket().getInetAddress().getHostAddress());
        connections.add(connection);
        System.out.println("Connection was added");
    }

    @Override
    public synchronized void connectionClosed(ConnectionInterface connection) {
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
    public synchronized void receivedContent(MessageInterface message) {
        textArea1.append(message.getNick() + ": " + message.getContent() + "\n");
        for (ConnectionInterface connection : connections) {
            connection.send(message);
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
            needToNewConnection = false;
            for (ConnectionInterface connection : connections) {
                connection.close();
            }
            connections = null;
            serverSocket.close();
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
    }
}
