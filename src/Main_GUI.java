import javax.swing.*;

public class Main_GUI {
    private JButton button1;
    private JButton button2;
    private JPanel panel_main;

    public Main_GUI(JFrame frame){
        frame.setContentPane(panel_main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
