import javax.naming.PartialResultException;
import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
@SuppressWarnings("InfiniteLoopStatement")
public class op_server implements Runnable{

    private Socket socket;
    private String op;
    private String username = null;
    private BufferedWriter writer;
    private BufferedReader reader;

    public op_server(Socket socket){
        this.socket = socket;
    }


    @Override
    public void run() {

        while(true){

            try{
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                while (!(op = reader.readLine()).isEmpty()){
                    String[] args = op.split(" ");

                    switch (args[0]) {
                        case ("login"):
                            login(args);
                            break;
                        case ("logout"):
                            logout(args);
                            break;
                        case ("listonlineusers"):
                            getonlineusers(args);
                            break;
                        case ("listproject"):
                            getlistproject(args);
                            break;
                        case ("createproject"):
                            createproject(args);
                            break;
                        case ("addmember"):
                            addmember(args);
                            break;
                        case ("showmembers"):
                            showmembers(args);
                            break;
                        case ("showcards"):
                            showcards(args);
                            break;
                        case ("showcard"):
                            showcard(args);
                            break;
                        case ("addcard"):
                            addcard(args);
                            break;
                        case ("movecard"):
                            movecard(args);
                            break;
                        case ("getcardhistory"):
                            getcardhistory(args);
                            break;
                        case ("cancelproject"):
                            cancelproject(args);
                            break;
                    }
                }

            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    public void login(String[] args){
        String username = args[1];
        String password = args[2];
        String ret = null;

        hash_users obj = singleton_db_utenti.getInstanceUtenti();
        if (obj.login(username,password)){
            this.username = username;
            ret = "SUCCESS "+obj.getUsers();
        }
        else{
            ret = "Username o Password errati";
        }
        sendanswer(ret);
    }

    public void logout(String[] args){
        String ret = null;

        if(this.username == null) ret = "Accesso negato a questa operazione";
        String username = args[1];
        if(!username.equals(this.username)) ret = "Permessi non sufficienti per disconnettere un altro utente";

        hash_users obj = singleton_db_utenti.getInstanceUtenti();
        ret = obj.logout(this.username);
        this.username = null;
        sendanswer(ret);
    }

    public void createproject(String[] args){
        String ret = null;
        if(this.username == null) ret = "Accesso negato a questa operazione";
        String projectname = args[1];

        hash_project obj = singleton_db_progetti.getInstanceProgetti();
        ret = obj.add_project(projectname,username);
        sendanswer(ret);
    }

    public void addmember(String[] args){
        String ret = null;
        if(this.username == null) ret = "Accesso negato a questa operazione";
        String projectname = args[1];
        String username = args[2];

        hash_project obj = singleton_db_progetti.getInstanceProgetti();
        ret = obj.add_projectmember(projectname, username);
        sendanswer(ret);
    }

    public void showmembers(String[] args){
        String ret = null;
        if(this.username == null) ret = "Accesso negato a questa operazione";
        String projectname = args[1];

        hash_project obj = singleton_db_progetti.getInstanceProgetti();
        LinkedList<String> membri = obj.show_members(projectname,this.username);
        Iterator<String> iterator = membri.iterator();
        while(iterator.hasNext()){
            ret = ret+iterator.next();
        }
        sendanswer("SUCCESS "+ret);
    }

    public void showcards(String[] args){
        String ret = null;
        if(this.username == null) ret = "Accesso negato a questa operazione";
        String projectname = args[1];

        hash_project obj = singleton_db_progetti.getInstanceProgetti();
        LinkedList<String> carte = obj.show_cards(projectname, this.username);
        Iterator<String> iterator = carte.iterator();
        while(iterator.hasNext()){
            ret = ret+iterator.next();
        }
        sendanswer(ret);
    }

    public void showcard(String[] args){
        String ret = null;
        if(this.username == null) ret = "Accesso negato a questa operazione";
        String projectname = args[1];
        String cardname = args[2];

        hash_project obj = singleton_db_progetti.getInstanceProgetti();
        ret = obj.show_card(projectname, cardname);
        sendanswer(ret);
    }

    public void addcard(String[] args){
        String projectname = args[1];
        String cardname = args[2];
        String descrizione = args[3];
        String ret = null;

        hash_project obj = singleton_db_progetti.getInstanceProgetti();
        ret = obj.add_card(projectname,cardname,descrizione);
        sendanswer(ret);
    }

    public void movecard(String[] args){
        String projectname = args[1];
        String cardname = args[2];
        String listapartenza = args[3];
        String listadestinazione = args[4];
        String ret = null;

        hash_project obj = singleton_db_progetti.getInstanceProgetti();
        ret = obj.move_card(projectname,cardname,listapartenza,listadestinazione);
        sendanswer(ret);
    }

    public void getcardhistory(String[] args){
        String projectname = args[1];
        String cardname = args[2];
        String ret = null;

        hash_project obj = singleton_db_progetti.getInstanceProgetti();
        ret = obj.get_cardhistory(projectname,cardname);
        sendanswer(ret);
    }

    public void cancelproject(String[] args){
        String projectname = args[1];
        String ret = null;

        hash_project obj = singleton_db_progetti.getInstanceProgetti();
        ret = obj.cancell_project(projectname);
        sendanswer(ret);
    }

    public void getonlineusers(String arg[]){
        String ret = null;
        if(this.username == null) ret = "Accesso negato a questa operazione";
        hash_users obj = singleton_db_utenti.getInstanceUtenti();
        ret = obj.get_onlineusers();
        sendanswer(ret);
    }

    public void getlistproject(String[] arg){
        String ret = null;
        if(this.username == null) ret = "Accesso negato a questa operazione";

        hash_users obj = singleton_db_utenti.getInstanceUtenti();
        ret = obj.get_listproject(username);
        sendanswer(ret);
    }

    public void sendanswer(String answer){
        try {
            writer.write(answer);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
