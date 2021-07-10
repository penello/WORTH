import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class HomeGui {
    private JTextField Projectnamefield;
    private JButton createProjectButton;
    private JButton projectMenùButton;
    private JPanel home_panel;
    private ClientManager clientManager;
    private JFrame mainFrame;
    private JFrame home;


    public HomeGui(ClientManager clientManager, JFrame start){
        this.clientManager = clientManager;
        this.mainFrame = start;
        home = new JFrame("WORTH home");
        initialize();
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
                //apro una vuova gui con il project menù
                MenuGui menugui = new MenuGui(clientManager,mainFrame);
                home.setVisible(false);
            }
        });
    }
}
