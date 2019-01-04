import javax.swing.*;
import java.awt.event.*;

public class Listener2 extends JDialog {
    private JPanel contentPane;
    private JButton buttonListen;
    private JButton buttonCancel;
    private JTextField textFieldNick;
    private JTextField textFieldPort;

    public Listener2(JFrame frame) {

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonListen);
        setResizable(false);
        setSize(260, 140);
        setLocationRelativeTo(frame);

        buttonListen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onListen();
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

    private void onListen() {
        System.out.println("Listen activate");
        try {
            System.out.println(textFieldNick.getText());
            System.out.println(Integer.parseInt(textFieldPort.getText()));
        } catch (Exception e) {
            System.out.println("Check your data in form");
        }
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
/*
        Listener2 dialog = new Listener2();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
        */
    }
}
