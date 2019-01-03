import javax.swing.*;

public class Listener extends JDialog {
    private JPanel panel1;
    private JTextField serverTextField;
    private JTextField a800TextField;
    private JButton listenButton;

    public void createListener() {
        JDialog frame = new JDialog(this, "App", true);
        frame.setContentPane(new Listener().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(this);
        frame.pack();
        frame.setVisible(true);
    }
}
