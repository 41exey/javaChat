package com.server;

import com.common.ConnectionInterface;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.net.Socket;
import java.util.Set;

public class Clients extends JDialog {
    private JPanel contentPane;
    private JButton buttonDelete;
    private JButton buttonCancel;

    public JList getList1() {
        return list1;
    }

    private JList<Item> list1;

    public DefaultListModel getListModel() {
        return listModel;
    }

    private DefaultListModel listModel;
    private JButton buttonBAN;

    public Clients(Server server) {
        setContentPane(contentPane);
        setModal(false);
        getRootPane().setDefaultButton(buttonDelete);
        setResizable(false);
        setSize(260, 240);
        setLocationRelativeTo((JFrame)server);
        setTitle("Clients");
        list1.setLayoutOrientation(JList.VERTICAL | JList.HORIZONTAL_WRAP);
        setStateComponents(false);

        listModel = new DefaultListModel();
        list1.setModel(listModel);

        buttonBAN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onBAN(server);
            }
        });

        buttonDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onDelete((Set<ConnectionInterface>) server.getConnections());
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
        list1.addComponentListener(new ComponentAdapter() {
        });
        list1.addComponentListener(new ComponentAdapter() {
        });
        list1.addContainerListener(new ContainerAdapter() {
        });
        list1.addFocusListener(new FocusAdapter() {
        });
        list1.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                setStateComponents(true);
            }
        });

    }

    public void setStateComponents(boolean state) {
        buttonDelete.setEnabled(state);
        buttonBAN.setEnabled(state);
    }

    private void onBAN(Server server) {
        Item item = list1.getSelectedValue();
        int index = list1.getSelectedIndex();

        if (index == -1)
            return;

        for (ConnectionInterface connection : server.connections) {
            if (connection.getSocket() == item.getSocket()) {
                connection.close();
                listModel.remove(index);
                server.banned.listModel.add(server.banned.listModel.getSize(),
                        item.getSocket().getInetAddress().getHostAddress());
                JOptionPane.showMessageDialog(contentPane,
                        item.toString() + " was banned",
                        "Clients",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }

        //dispose();
        //setVisible(false);
    }

    private void onDelete(Set<ConnectionInterface> connections) {

        Item item = list1.getSelectedValue();
        int index = list1.getSelectedIndex();
        for (ConnectionInterface connection : connections) {
            if (connection.getSocket() == item.getSocket()) {
                connection.close();
                JOptionPane.showMessageDialog(contentPane,
                        item.toString() + " was removed",
                        "Clients",
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            }

        }

        //dispose();
        //System.out.println(item.toString());
        //item.setNick("New Nick");
        //list1.setSelectedValue(item, true);
        System.out.println("Index: " + index);
        if (index == -1)
            return;

        listModel.remove(index);

        //setVisible(false);
    }

    private void onCancel() {
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

class Item {
    public void setNick(String nick) {
        this.nick = nick;
    }

    @Override
    public String toString() {
        return  nick +
                " (" + IP + ")";
    }

    private String nick;
    private String IP;

    public Socket getSocket() {
        return socket;
    }

    private Socket socket;
    Item(String nick, String IP, Socket socket) {
        this.nick = nick;
        this.IP = IP;
        this.socket = socket;
    }
}
