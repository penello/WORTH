import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.rmi.RemoteException;

public class MenuGui {
    private ClientManager clientManager;
    private JFrame mainFrame;
    private JFrame menu;
    private JPanel menu_panel;
    private JPanel addmember_panel;
    private JPanel addcard_panel;
    private JPanel movecard_panel;
    private JPanel showcards_panel;
    private JPanel showcard_panel;
    private JPanel listmember_panel;
    private JTextField username_addmember_field;
    private JTextField cardname_addcard_field;
    private JTextField descrizione_addcard_field;
    private JTextField cardname_movecard_field;
    private JTextField listapartenza_movecard_field;
    private JTextField listadestinazione_movecard_field;
    private JTextField cardname_showcard_field;
    private JPanel cardhistory;
    private JTextField cardname_cardhistory_field;
    private JButton GOButton_addmember;
    private JButton GOButton_addcard;
    private JButton GOButton_movecard;
    private JButton GOButton_showcards;
    private JButton GOButton_showcard;
    private JButton GOButton_listmember;
    private JButton GOButton_history;
    private JTabbedPane tabbedPane1;
    private JLabel projectMenùLabel;
    private JButton logOutButton;
    private JPanel Chat;
    private JTextArea chatbox;
    private JTextField messagge_field;
    private JButton sendButton;
    private JTextArea textArea_cardhistory;
    private JTextArea textArea_showcard;
    private JTextArea textArea_listmember;
    private JTextArea textArea_showcards;
    private JTextArea textArea_movecard;
    private JTextArea textArea_addcard;
    private JTextArea textArea_addmember;
    private JPanel Calcel_Project;
    private JTextArea textArea_cancelproject;
    private JButton GOButton_cancelproject;
    private JButton INDIETROButton;
    private String projectname;
    private String username;
    private Chat chatThread;



    public MenuGui(ClientManager clientmanager, JFrame start,String projectname, String username){
        this.projectname = projectname;
        this.username = username;
        this.clientManager = clientmanager;
        this.mainFrame = start;
        menu = new JFrame("project menù");
        setallfalse();
        initialize();
        chatbox.setEditable(false);
        textArea_cardhistory.setEditable(false);
        textArea_showcard.setEditable(false);
        textArea_listmember.setEditable(false);
        textArea_showcards.setEditable(false);
        textArea_movecard.setEditable(false);
        textArea_addcard.setEditable(false);
        textArea_addmember.setEditable(false);
        textArea_cancelproject.setEditable(false);
        try{
            String chatAddress = clientManager.get_chat_multicast(projectname);
            if(chatAddress.startsWith("SUCCESS ")){
                chatAddress = chatAddress.replace("SUCCESS ","");
                chatThread = new Chat(chatAddress,chatbox,username);
                chatThread.start();
            }
            else{
                chatbox.append(chatAddress);
            }
        }
        catch(IOException e){
            chatbox.append("Impossibile Collegarsi alla chat");
        }
        menu.setLocationRelativeTo(null);
        menu.setVisible(true);
    }

    public void setallfalse(){
        addmember_panel.setVisible(false);
        addcard_panel.setVisible(false);
        movecard_panel.setVisible(false);
        showcards_panel.setVisible(false);
        showcard_panel.setVisible(false);
        listmember_panel.setVisible(false);
        cardhistory.setVisible(false);
    }


    private void initialize() {
        menu.setContentPane(menu_panel);
        //menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.addWindowListener(new WindowAdapter() {

            @Override

            public void windowClosing(WindowEvent e) {
                try {
                    clientManager.close();
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
                if(chatThread != null){
                    chatThread.close();
                }
                try {
                    clientManager.logout(username);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                System.exit(0);
            }

        });
        menu.pack();
        projectMenùLabel.setText(projectname);
        GOButton_addmember.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = username_addmember_field.getText().trim();
                if(projectname.isEmpty() || username.isEmpty()){
                    JOptionPane.showMessageDialog(null, "projectname o username non validi! Riprova", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try{
                    String esito = clientManager.addmember(projectname,username);
                    if(esito.startsWith("SUCCESS")){
                        //aggiunta del membro andata buon fine, faccio il login
                        textArea_addmember.append(esito+"\n");
                    }
                    else{
                        textArea_addmember.append("ERRORE:" + esito+"\n");
                    }
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Server disconnesso!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        });

        GOButton_addcard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cardname = cardname_addcard_field.getText().trim();
                String descrizione = descrizione_addcard_field.getText().trim();
                descrizione = descrizione.replace(" ","");
                if(projectname.isEmpty() || cardname.isEmpty() || descrizione.isEmpty()){
                    JOptionPane.showMessageDialog(null, "projectname o username non validi! Riprova", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try{
                    String esito = clientManager.addCard(projectname,cardname,descrizione);
                    if(esito.startsWith("SUCCESS")){
                        //aggiunta della carta andata buon fine, faccio il login
                        textArea_addcard.append(esito+"\n");
                    }
                    else{
                        textArea_addcard.append("ERRORE:" + esito+"\n");
                    }
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Server disconnesso!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        });

        GOButton_movecard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cardname = cardname_movecard_field.getText().trim();
                String listapartenza = listapartenza_movecard_field.getText().trim();
                String listadestinazione = listadestinazione_movecard_field.getText().trim();
                if(projectname.isEmpty() || cardname.isEmpty() || listapartenza.isEmpty() || listadestinazione.isEmpty()){
                    JOptionPane.showMessageDialog(null, "projectname o username non validi! Riprova", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try{
                    String esito = clientManager.moveCard(projectname,cardname,listapartenza,listadestinazione);
                    if(esito.startsWith("SUCCESS")){
                        //carta spostata con successo
                        textArea_movecard.append(esito+"\n");
                    }
                    else{
                        textArea_movecard.append("ERRORE:" + esito+"\n");
                    }
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Server disconnesso!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        });

        GOButton_showcards.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(projectname.isEmpty()){
                    JOptionPane.showMessageDialog(null, "projectname o username non validi! Riprova", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try{
                    String esito = clientManager.showcards(projectname);
                    if(esito.startsWith("SUCCESS")){
                        //lista ricevuta con successo
                        textArea_showcards.append(esito+"\n");
                    }
                    else{
                        textArea_showcards.append("ERRORE:" + esito+"\n");
                    }
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Server disconnesso!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        });

        GOButton_showcard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cardname = cardname_showcard_field.getText().trim();
                if(projectname.isEmpty() || cardname.isEmpty()){
                    JOptionPane.showMessageDialog(null, "projectname o username non validi! Riprova", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try{
                    String esito = clientManager.showcard(projectname,cardname);
                    if(esito.startsWith("SUCCESS")){
                        //carta ricevuta con successo
                        textArea_showcard.append(esito+"\n");
                    }
                    else{
                        textArea_showcard.append("ERRORE" + esito+"\n");
                    }
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Server disconnesso!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        });

        GOButton_listmember.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(projectname.isEmpty()){
                    JOptionPane.showMessageDialog(null, "projectname o username non validi! Riprova", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try{
                    String esito = clientManager.showmembers(projectname);
                    if(esito.startsWith("SUCCESS")){
                        //lista membri ricevuta con successo
                        textArea_listmember.append(esito+"\n");
                    }
                    else{
                        textArea_listmember.append("ERRORE" + esito+"\n");
                    }
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Server disconnesso!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        });

        GOButton_history.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cardname = cardname_cardhistory_field.getText().trim();
                if(projectname.isEmpty() || cardname.isEmpty()){
                    JOptionPane.showMessageDialog(null, "projectname o username non validi! Riprova", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try{
                    String esito = clientManager.getCardHistory(projectname,cardname);
                    if(esito.startsWith("SUCCESS")){
                        //lista movimenti della card ricevuta con successo
                        textArea_cardhistory.append(esito+"\n");
                    }
                    else{
                        textArea_cardhistory.append("ERRORE" + esito+"\n");
                    }
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Server disconnesso!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!messagge_field.getText().trim().isEmpty()){
                    try{
                        chatThread.sendMessage(messagge_field.getText().trim());
                        messagge_field.setText("");
                    }
                    catch (IOException exception){
                        chatbox.append("Impossibile Mandare il messaggio\n");
                    }

                }

            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main_GUI main_gui = new Main_GUI(clientManager);
                try {
                    if(chatThread != null){
                        chatThread.close();
                    }
                    clientManager.logout(username);
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Server disconnesso!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
                menu.setVisible(false);
            }
        });

        messagge_field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                int key=e.getKeyCode();
                if(key==KeyEvent.VK_ENTER){
                    sendButton.doClick();
                }
            }
        });

        GOButton_cancelproject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(projectname.isEmpty()){
                    JOptionPane.showMessageDialog(null, "projectname o username non validi! Riprova", "Error Message",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try{
                    String esito = clientManager.cancelProject(projectname);
                    if(esito.startsWith("SUCCESS")){
                        //lprogetto cancellato correttamente
                        textArea_cancelproject.append(esito+"\n");
                    }
                    else{
                        textArea_cancelproject.append("ERRORE" + esito+"\n");
                    }
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(null, "Server disconnesso!", "Error Message", JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            }
        });

        INDIETROButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HomeGui home_gui = new HomeGui(clientManager,mainFrame,username);
                if(chatThread != null){
                    chatThread.close();
                }
                menu.setVisible(false);
            }
        });

    }

}
