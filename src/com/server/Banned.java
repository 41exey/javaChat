package com.server;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;

public class Banned extends JDialog {
    private JPanel contentPane;
    private JButton buttonUnBAN;
    private JButton buttonCancel;
    private JList<String> list1;
    DefaultListModel listModel;

    public Banned(Server server) {
        setContentPane(contentPane);
        setModal(false);
        getRootPane().setDefaultButton(buttonUnBAN);
        setResizable(false);
        setSize(260, 240);
        setLocationRelativeTo((JFrame)server);
        setTitle("Clients");
        list1.setLayoutOrientation(JList.VERTICAL | JList.HORIZONTAL_WRAP);
        setStateComponents(false);

        listModel = new DefaultListModel();
        list1.setModel(listModel);

        buttonUnBAN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onUnBAN();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        /*
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        */
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setStateComponents(true);
            }
        });
    }

    public void setStateComponents(boolean state) {
        buttonUnBAN.setEnabled(state);
    }

    private void onUnBAN() {
        String item = list1.getSelectedValue();
        int index = list1.getSelectedIndex();

        if (index == -1)
            return;

        listModel.remove(index);
        JOptionPane.showMessageDialog(contentPane,
                item + " un banned",
                "Clients",
                JOptionPane.INFORMATION_MESSAGE);

        //dispose();
        //setVisible(false);
    }

    private void onCancel() {
        // add your code here if necessary
        //dispose();
        setVisible(false);
    }

    public static void main(String[] args) {
        /*
        Banned dialog = new Banned();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
        */
    }
}
