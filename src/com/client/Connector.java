package com.client;

import com.common.Connection;
import com.common.Message;

import javax.swing.*;
import java.awt.event.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

public class Connector extends JDialog {
    private JPanel contentPane;
    private JButton buttonConnect;
    private JButton buttonCancel;
    private JTextField textFieldNick;
    private JTextField textFieldPort;
    private JTextField textFieldIP;

    public void setDisableComponents() {
        textFieldNick.setEnabled(false);
        textFieldPort.setEnabled(false);
        textFieldIP.setEnabled(false);
    }

    public void setEnableComponent() {
        textFieldNick.setEnabled(true);
        textFieldPort.setEnabled(true);
        textFieldIP.setEnabled(true);
    }

    public Connector(Client form) {

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonConnect);
        setResizable(false);
        setSize(260, 170);
        setLocationRelativeTo((JFrame)form);
        setTitle("Connector");

        textFieldNick.setText(form.getNickname());
        textFieldPort.setText(Integer.toString(form.getPort()));
        textFieldIP.setText(form.getIP());

        if (form.connect) {
//            System.out.println("Active");
            buttonConnect.setText("Disconnect");
            setDisableComponents();

//                  form.listen = false;
        } else {
//            System.out.println("Un Active");
            buttonConnect.setText("Connect");
            setEnableComponent();
        }

       /*
        setContentPane(contentPane);
        setModal(true);
        */
        getRootPane().setDefaultButton(buttonConnect);


        buttonConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onConnect(form);
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });


/*
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        */
        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    }

    private void onConnect(Client form) {
        //System.out.println("Listen activate");
        if (!form.connect) {
            try {
                System.out.println(textFieldNick.getText());
                System.out.println(Integer.parseInt(textFieldPort.getText()));
                System.out.println(textFieldIP.getText());
                form.setNickname(textFieldNick.getText().trim());
                form.setPort(Integer.parseInt(textFieldPort.getText().trim()));
                form.setIP(textFieldIP.getText().trim());
                Socket socket = new Socket(InetAddress.getByName(form.getIP()), form.getPort());
                form.connection = new Connection(socket, form);
                form.connectionCreated(form.connection);

                form.clearTextArea();
                form.setEnableComponents();
                form.connect = true;

                dispose();
            } catch (ConnectException e) {
                JOptionPane.showMessageDialog(form,
                        "Connection refused",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            buttonConnect.setText("Listen");
            setEnableComponent();
            form.setDisableComponents();
            form.connectionClosed(form.connection);
            form.connect = false;
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        /*
        Connector dialog = new Connector();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
        */
    }
}
