package com.server;

import javax.swing.*;
import java.awt.event.*;

public class Clients extends JDialog {
    private JPanel contentPane;
    private JButton buttonDelete;
    private JButton buttonCancel;
    private JList list1;
    private JCheckBox withHostNameCheckBox;
    private JButton banButton;

    public Clients(Server form) {
        setContentPane(contentPane);
        setModal(false);
        getRootPane().setDefaultButton(buttonDelete);
        setResizable(false);
        setSize(260, 240);
        setLocationRelativeTo((JFrame)form);
        setTitle("Clients");

        buttonDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDelete();
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
    }

    public void setStateComponents(boolean state) {
        buttonDelete.setEnabled(state);
    }

    private void onDelete() {
        // add your code here
        //dispose();
        setVisible(false);
    }

    private void onCancel() {
        // add your code here if necessary
        //dispose();
        setVisible(false);
    }

    public static void main(String[] args) {
        /*
        Clients dialog = new Clients();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
        */
    }
}
