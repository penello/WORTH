import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main_GUI {
    private JButton LOGINButton;
    private JButton SIGNINButton;
    private JPanel panel_main;
    private JLabel WORTHLabel;
    private JTextField Usernamefield;
    private JTextField Passwordfield;
    private ClientManager clientManager;
    private JFrame mainFrame;

    //public Main_GUI(JFrame frame){
    public Main_GUI(ClientManager clientManager) {
        this.clientManager = clientManager;
        mainFrame = new JFrame("WORTH");
        initialize();
        mainFrame.setVisible(true);
    }

    private void initialize() {
        mainFrame.setContentPane(panel_main);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        LOGINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = Usernamefield.getText().trim();
                String password = Passwordfield.getText().trim();

                JOptionPane.showMessageDialog(null, "ci Symi?");
                //do something


            }
        });
        SIGNINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = Usernamefield.getText().trim();
                String password = Passwordfield.getText().trim();

                JOptionPane.showMessageDialog(null, "u ci Symi?");
                //do something


            }
        });
    }
}
