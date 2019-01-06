package com.server;

import com.common.ConnectionInterface;

import javax.swing.*;
import java.awt.event.*;
import java.net.ServerSocket;
import java.util.LinkedHashSet;

public class Listener extends JDialog {
    private JPanel contentPane;
    private JButton buttonListen;
    private JButton buttonCancel;
    private JTextField textFieldNick;
    private JTextField textFieldPort;

    public void setDisableComponents() {
        textFieldNick.setEnabled(false);
        textFieldPort.setEnabled(false);
    }

    public void setEnableComponent() {
        textFieldNick.setEnabled(true);
        textFieldPort.setEnabled(true);
    }

    public Listener(Server form) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonListen);
        setResizable(false);
        setSize(260, 140);
        setLocationRelativeTo((JFrame)form);
        setTitle("Listener");

        textFieldNick.setText(form.getNickname());
        textFieldPort.setText(Integer.toString(form.getPort()));

        if (form.listen) {
            System.out.println("Active");
            buttonListen.setText("Un listen");
            setDisableComponents();

//                  form.listen = false;
        } else {
            System.out.println("Un Active");
            buttonListen.setText("Listen");
            setEnableComponent();
        }

        buttonListen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onListen(form);
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
//
//        // call onCancel() when cross is clicked
//        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
//        addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                onCancel();
//            }
//        });
//
//        // call onCancel() on ESCAPE
//        contentPane.registerKeyboardAction(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                onCancel();
//            }
//        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onListen(Server form) {
        //System.out.println("Listen activate");
        if (!form.listen) {
            try {
                System.out.println(textFieldNick.getText().trim());
                System.out.println(Integer.parseInt(textFieldPort.getText().trim()));
                form.setNickname(textFieldNick.getText().trim());
                form.setPort(Integer.parseInt(textFieldPort.getText().trim()));

                form.serverSocket = new ServerSocket(form.getPort());
                form.connections = new LinkedHashSet();
                form.start();


                form.clearTextArea();
                form.setEnableComponents();
                form.listen = true;


                dispose();

            } catch (Exception e) {
                System.out.println("Check your data in form");
                e.printStackTrace();
            }
        } else {
            try {
//                form.serverSocket.close();
                form.stop();
//                form.connections = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            buttonListen.setText("Listen");
            setEnableComponent();
            form.setDisableComponents();
            form.listen = false;
        }
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
/*
        Listener dialog = new Listener();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
        */
    }
}
