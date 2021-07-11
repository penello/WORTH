import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class HomeGui {
    private JTextField Projectnamefield;
    private JButton createProjectButton;
    private JButton projectMenùButton;
    private JPanel home_panel;
    private JLabel Projectimg;
    private JButton logOutButton;
    private ClientManager clientManager;
    private JFrame mainFrame;
    private JFrame home;
    private String username;


    public HomeGui(ClientManager clientManager, JFrame start,String username){
        this.username = username;
        this.clientManager = clientManager;
        this.mainFrame = start;
        home = new JFrame("WORTH home");
        initialize();
        home.setLocationRelativeTo(null);
        home.setVisible(true);
    }

    private void initialize() {
        home.setContentPane(home_panel);
        home.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        home.pack();
        createProjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String projectname = Projectnamefield.getText().trim();
                if(projectname.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Utente o password non validi! Riprova", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try{
                    String esito = clientManager.createproject(projectname);
                    if(esito.startsWith("SUCCESS ")){
                        //progetto creato correttamente
                        JOptionPane.showMessageDialog(null, esito);
                        projectMenùButton.doClick();
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "Progetto non creato: " + esito , "Error Message" , JOptionPane.ERROR_MESSAGE);
                    }

                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Server disconnesso!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        });

        projectMenùButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String projectname = Projectnamefield.getText().trim();
                if(projectname.isEmpty()){
                    JOptionPane.showMessageDialog(null, "Utente o password non validi! Riprova", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                boolean isContained= false;
                try {
                    String [] users = clientManager.showmembers(projectname).replace("SUCCESS ","").split("\\|");
                    ArrayList<String> list = new ArrayList<>(Arrays.asList(users));
                    isContained = list.contains(username);
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Server disconnesso!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
                if(isContained){
                    //apro una vuova gui con il project menù
                    MenuGui menugui = new MenuGui(clientManager,mainFrame,projectname,username);
                    home.setVisible(false);
                }
                else{
                    JOptionPane.showMessageDialog(null,"Accesso negato, non sei membro del progetto " + projectname);
                }
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main_GUI main_gui = new Main_GUI(clientManager);
                try {
                    clientManager.logout(username);
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Server disconnesso!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
                home.setVisible(false);
            }
        });
    }

    private void createUIComponents() {
        Projectimg = new JLabel(new ImageIcon("project-planning.png"));
    }
}
