import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

public class Main_GUI {
    private JButton LOGINButton;
    private JButton SIGNINButton;
    private JPanel panel_main;
    private JLabel WORTHLabel;
    private JTextField Usernamefield;
    private JPasswordField passwordField1;
    private JLabel Imagelogo;
    private JLabel Passwordlogo;
    private ClientManager clientManager;
    private JFrame mainFrame;
    private String username;
    private String password;

    //public Main_GUI(JFrame frame){
    public Main_GUI(ClientManager clientManager) {
        this.clientManager = clientManager;
        mainFrame = new JFrame("WORTH");
        initialize();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }


    private void initialize() {
        mainFrame.setContentPane(panel_main);
        //mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.addWindowListener(new WindowAdapter() {

            @Override

            public void windowClosing(WindowEvent e) {
                try {
                    clientManager.close();
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
                System.exit(0);
            }
        });

        mainFrame.pack();

        LOGINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                get_text();
                try{
                    String esito = clientManager.login(username,password);
                    if(esito.startsWith("SUCCESS")){
                        //login eseguito correttamente
                        HomeGui homegui = new HomeGui(clientManager, mainFrame,username);
                        mainFrame.setVisible(false);
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Login non andato a buon fine: " + esito , "Error Message" , JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Server disconnesso!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }

            }
        });
        SIGNINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                get_text();
                try{
                    String esito = clientManager.register(username,password);
                    if(esito.startsWith("SUCCESS")){
                        //registrazione andata buon fine, faccio il login
                        LOGINButton.doClick();
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Registrazione non andata abuon fine: " + esito , "Error Message" ,JOptionPane.ERROR_MESSAGE);
                    }
                } catch (RemoteException remoteException) {
                    JOptionPane.showMessageDialog(null, "Server disconnesso!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        });

        passwordField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key=e.getKeyCode();
                if(key==KeyEvent.VK_ENTER){
                    LOGINButton.doClick();
                }
            }
        });
    }

    private void get_text(){
        username = Usernamefield.getText().trim();
        password = passwordField1.getText().trim();
        if(username.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(null, "Utente o password non validi! Riprova", "Error Message",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    private void createUIComponents() {
        Imagelogo = new JLabel(new ImageIcon("admin-icon.png"));
        Passwordlogo = new JLabel(new ImageIcon("password.png"));
    }
}
