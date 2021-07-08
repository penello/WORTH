import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

@SuppressWarnings("InfiniteLoopStatement")
public class Op_server implements Runnable{

    private Socket socket;
    private String op;
    private String username = null;
    private String access_denided ="Accesso negato per questa operazione";
    private String save_failed = "impossibile salvare in maniera persistete i dati";
    private BufferedWriter writer;



    public Op_server(Socket socket){
        this.socket = socket;
    }


    @Override
    public void run() {
        while(true){

            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
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

        Hash_users obj = Singleton_db_utenti.getInstanceUtenti();
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
        String username = args[1];
        if(this.username == null) ret = access_denided;
        else if(!username.equals(this.username)) ret = access_denided;
        else {
            Hash_users obj = Singleton_db_utenti.getInstanceUtenti();
            ret = obj.logout(this.username);
            this.username = null;
        }
        sendanswer(ret);
    }

    public void createproject(String[] args){
        String ret = null;
        String projectname = args[1];
        if(this.username == null) ret = access_denided;
        else {
            Hash_project obj = Singleton_db_progetti.getInstanceProgetti();
            ret = obj.add_project(projectname, username);
            Persistent_data prs = Persistent_data.getInstance();
            if (!prs.create_dir(projectname)) ret = save_failed;
            if (!prs.save(prs.getProject_folder() + projectname + "/membri.json", obj.get_project(projectname).getMembri(), LinkedList.class)) ret = save_failed;
        }
        sendanswer(ret);
    }

    public void addmember(String[] args){
        String ret = null;
        String projectname = args[1];
        String username = args[2];
        if(this.username == null) ret = access_denided;
        else if(!Singleton_db_progetti.getInstanceProgetti().get_project(projectname).containsmember(this.username)) ret = access_denided;
        else {
            Hash_project obj = Singleton_db_progetti.getInstanceProgetti();
            ret = obj.add_projectmember(projectname, username);
            Persistent_data prs = Persistent_data.getInstance();
            if (!prs.save(prs.getProject_folder() + projectname + "/membri.json", obj.get_project(projectname).getMembri(), LinkedList.class)) ret = save_failed;
            if (!prs.save(prs.getUser_folder() + username + "utenti.json", Singleton_db_utenti.getInstanceUtenti(), Hash_users.class)) ret = save_failed;
        }
        sendanswer(ret);
    }

    public void showmembers(String[] args){
        String ret = null;
        String projectname = args[1];
        if(this.username == null) ret = access_denided;
        else if(!Singleton_db_progetti.getInstanceProgetti().get_project(projectname).containsmember(this.username)) ret = access_denided;
        else {
            Hash_project obj = Singleton_db_progetti.getInstanceProgetti();
            LinkedList<String> membri = obj.show_members(projectname, this.username);
            Iterator<String> iterator = membri.iterator();
            while (iterator.hasNext()) {
                ret = ret + iterator.next();
            }
        }
        sendanswer("SUCCESS "+ret);
    }

    public void showcards(String[] args){
        String ret = null;
        String projectname = args[1];
        if(this.username == null) ret = access_denided;
        else if(!Singleton_db_progetti.getInstanceProgetti().get_project(projectname).containsmember(this.username)) ret = access_denided;
        else {
            Hash_project obj = Singleton_db_progetti.getInstanceProgetti();
            LinkedList<String> carte = obj.show_cards(projectname, this.username);
            Iterator<String> iterator = carte.iterator();
            while (iterator.hasNext()) {
                ret = ret + iterator.next();
            }
        }
        sendanswer(ret);
    }

    public void showcard(String[] args){
        String ret = null;
        String projectname = args[1];
        String cardname = args[2];
        if(this.username == null) ret = access_denided;
        else if(!Singleton_db_progetti.getInstanceProgetti().get_project(projectname).containsmember(this.username)) ret = access_denided;
        else {
            Hash_project obj = Singleton_db_progetti.getInstanceProgetti();
            ret = obj.show_card(projectname, cardname);
        }
        sendanswer(ret);
    }

    public void addcard(String[] args){
        String ret =null;
        String projectname = args[1];
        String cardname = args[2];
        String descrizione = args[3];
        if(this.username == null) ret = access_denided;
        else if(!Singleton_db_progetti.getInstanceProgetti().get_project(projectname).containsmember(this.username)) ret = access_denided;
        else {
            Hash_project obj = Singleton_db_progetti.getInstanceProgetti();
            ret = obj.add_card(projectname, cardname, descrizione);
            Persistent_data prs = Persistent_data.getInstance();
            if(!prs.save(prs.getProject_folder()+projectname+"/"+cardname+".json", Singleton_db_progetti.getInstanceProgetti().get_project(projectname).Getcard(cardname), Card.class)) ret = save_failed;
        }
        sendanswer(ret);
    }

    public void movecard(String[] args){
        String ret = null;
        String projectname = args[1];
        String cardname = args[2];
        String listapartenza = args[3];
        String listadestinazione = args[4];
        if(this.username == null) ret = access_denided;
        else if(!Singleton_db_progetti.getInstanceProgetti().get_project(projectname).containsmember(this.username)) ret = access_denided;
        else {
            Hash_project obj = Singleton_db_progetti.getInstanceProgetti();
            ret = obj.move_card(projectname, cardname, listapartenza, listadestinazione);
            Persistent_data prs = Persistent_data.getInstance();
            if(!prs.save(prs.getProject_folder()+projectname+"/"+cardname+".json", Singleton_db_progetti.getInstanceProgetti().get_project(projectname).Getcard(cardname), Card.class)) ret = save_failed;
        }
        sendanswer(ret);
    }

    public void getcardhistory(String[] args){
        String ret = null;
        String projectname = args[1];
        String cardname = args[2];
        if(this.username == null) ret = access_denided;
        else if (!Singleton_db_progetti.getInstanceProgetti().get_project(projectname).containsmember(this.username)) ret = access_denided;
        else {
            Hash_project obj = Singleton_db_progetti.getInstanceProgetti();
            ret = obj.get_cardhistory(projectname, cardname);
        }
        sendanswer(ret);
    }

    public void cancelproject(String[] args){
        String projectname = args[1];
        String ret = null;
        if(this.username == null) ret = access_denided;
        else if(!Singleton_db_progetti.getInstanceProgetti().get_project(projectname).containsmember(this.username)) ret = access_denided;
        else {
            Hash_project obj = Singleton_db_progetti.getInstanceProgetti();
            ret = obj.cancell_project(projectname,this.username);
            if(ret.startsWith("SUCCESS")) {
                Persistent_data prs = Persistent_data.getInstance();
                try {
                    File directory = new File((prs.getProject_folder() + projectname));
                    for (File file : Objects.requireNonNull(directory.listFiles()))
                        file.delete();
                    directory.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                    ret = "impossibile eliminare la persistenza del progetto";
                }
            }
        }
        sendanswer(ret);
    }

    public void getonlineusers(String arg[]){
        String ret = null;
        if(this.username == null) ret = access_denided;
        else {
            Hash_users obj = Singleton_db_utenti.getInstanceUtenti();
            ret = obj.get_onlineusers();
        }
        sendanswer(ret);
    }

    public void getlistproject(String[] arg){
        String ret = null;
        if(this.username == null) ret = access_denided;
        else {
            Hash_users obj = Singleton_db_utenti.getInstanceUtenti();
            ret = obj.get_listproject(username);
        }
        sendanswer(ret);
    }

    private void sendanswer(String answer){
        try {
            writer.write(answer + "\n\r\n");
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


}
