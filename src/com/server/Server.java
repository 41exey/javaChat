package com.server;

import com.common.Connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Server extends JFrame {

    JPanel panel1;
    private JTextArea textArea1;
    private JTextField textField1;
    private JButton buttonSend;
    private JScrollPane scrollPane;
    boolean listen;

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
//                Listener2 listener = new Listener2(Form.this);
//                listener.setVisible(true);
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
                    System.out.println(textField1.getText());
                    textArea1.append(getNickname() + ": " + textField1.getText() + "\n");
                    textField1.setText("");
                }
            }
        });
    }
}
