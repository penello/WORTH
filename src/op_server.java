import javax.naming.PartialResultException;
import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class op_server implements Runnable{

    private Socket socket;
    private String op;
    private String username = null;

    public op_server(Socket socket){
        this.socket = socket;
    }


    @Override
    public void run() {

        while(true){

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

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

    public String login(String[] args){
        String username = args[1];
        String password = args[2];
        String ret;

        hash_users obj = singleton_db.getInstanceUtenti();
        if (obj.login(username,password)){
            this.username = username;
            ret = "SUCCESS "+obj.getUsers();
        }
        else{
            ret = "Username o Password errati";
        }
        return ret;

        //RITORNARE LA LISTA DEGLI UTENTI REGISTRATI, CHIEDERE A FRANESCO PIRRò HIHIHIHIHIHIHIH

    }

    //qui per effettuare il logout voglio pure la password perchè non so come gestire il fatto che magari lui voglia disconnettere un altro utente dandomi solo il nickname
    //TODO match utente con il socket
    public String logout(String[] args){
        if(this.username == null) return "Accesso negato a questa operazione";

        String ret;

        hash_users obj = singleton_db.getInstanceUtenti();
        ret = obj.logout(this.username);
        this.username = null;
        return ret;
    }

    //TODO: String username = match con socket;
    public String createproject(String[] args){
        if(this.username == null) return "Accesso negato a questa operazione";
        String projectname = args[1];
        String ret;

        //TODO: prendere username dal socket
        hash_project obj = singleton_db.getInstanceProgetti();
        ret = obj.add_project(projectname,username);
        return ret;
    }

    public String addmember(String[] args){
        if(this.username == null) return "Accesso negato a questa operazione";
        String projectname = args[1];
        String username = args[2];
        String ret;

        hash_project obj = singleton_db.getInstanceProgetti();
        ret = obj.add_projectmember(projectname, username);
        return ret;
    }

    public String showmembers(String[] args){
        if(this.username == null) return "Accesso negato a questa operazione";
        String projectname = args[1];
        String ret = "SUCCESS ";

        hash_project obj = singleton_db.getInstanceProgetti();
        LinkedList<String> membri = obj.show_members(projectname,this.username);
        Iterator<String> iterator = membri.iterator();
        while(iterator.hasNext()){
            ret = ret+iterator.next();
        }
        return ret;
    }

    public String showcards(String[] args){
        if(this.username == null) return "Accesso negato a questa operazione";
        String projectname = args[1];
        String ret = "SUCCESS ";

        hash_project obj = singleton_db.getInstanceProgetti();
        LinkedList<String> carte = obj.show_cards(projectname, this.username);
        Iterator<String> iterator = carte.iterator();
        while(iterator.hasNext()){
            ret = ret+iterator.next();
        }
        return ret;
    }

    public String showcard(String[] args){
        if(this.username == null) return "Accesso negato a questa operazione";
        String projectname = args[1];
        String cardname = args[2];
        String ret;

        hash_project obj = singleton_db.getInstanceProgetti();
        ret = obj.show_card(projectname, cardname);
        return ret;
    }

    public String addcard(String[] args){
        String projectname = args[1];
        String cardname = args[2];
        String descrizione = args[3];
        String ret;

        hash_project obj = singleton_db.getInstanceProgetti();
        ret = obj.add_card(projectname,cardname,descrizione);
        return ret;
    }

    public String movecard(String[] args){
        String projectname = args[1];
        String cardname = args[2];
        String listapartenza = args[3];
        String listadestinazione = args[4];
        String ret;

        hash_project obj = singleton_db.getInstanceProgetti();
        ret = obj.move_card(projectname,cardname,listapartenza,listadestinazione);
        return ret;
    }

    public String getcardhistory(String[] args){
        String projectname = args[1];
        String cardname = args[2];
        String ret;

        hash_project obj = singleton_db.getInstanceProgetti();
        ret = obj.get_cardhistory(projectname,cardname);
        return ret;
    }

    public String cancelproject(String[] args){
        String projectname = args[1];
        String ret;

        hash_project obj = singleton_db.getInstanceProgetti();
        ret = obj.cancell_project(projectname);
        return ret;
    }

    public String getonlineusers(String arg[]){
        if(this.username == null) return "Accesso negato a questa operazione";
        String ret;
        hash_users obj = singleton_db.getInstanceUtenti();
        ret = obj.get_onlineusers();
        return ret;
    }

    //TODO: match socket con username grazie mille
    public String getlistproject(String[] arg){
        if(this.username == null) return "Accesso negato a questa operazione";
        String ret;

        hash_users obj = singleton_db.getInstanceUtenti();
        ret = obj.get_listproject(username);
        return ret;
    }




}
