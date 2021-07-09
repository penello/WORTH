import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main_GUI {
    private JPanel panel_main;
    private JButton SIGNINButton;
    private JButton LOGINButton;
    private JLabel WORTHLabel;
    private JTextField Username;
    private JTextField Password;

    public Main_GUI(JFrame frame){
        frame.setContentPane(panel_main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        LOGINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String username = Username.getText();
                String password = Password.getText();
            }
        });
    }
}
