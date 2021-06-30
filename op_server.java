import java.io.*;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;

public class op_server implements Runnable{

    private Socket socket;
    private String op;

    public op_server(Socket socket){
        this.socket = socket;
    }


    @Override
    public void run() {

        while(true){

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                while ((op = reader.readLine()).isEmpty()!=false ){
                    String[] args = op.split(" ");

                    switch (args[0]) {
                        case ("login"):
                            login(args);
                            break;
                        case ("logout"):
                            logout(args);
                            break;
                        case ("listusers"):
                            getlistusers();
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
                        case ("readchat"):
                            readchat(args);
                            break;
                        case ("sendchatmsg"):
                            sendchatmsg(args);
                            break;
                        case ("cancelproject"):
                            cancelproject(args);
                            break;
                    }





                }

            }catch (IOException e){

            }
        }

    }

    public String login(String[] args){
        String username = args[1];
        String password = args[2];
        String ret;

        hash_users obj = singleton_db.getInstanceUtenti();
        ret = obj.login(username,password);
        return ret;

        //RITORNARE LA LISTA DEGLI UTENTI REGISTRATI, CHIEDERE A FRANESCO PIRRò HIHIHIHIHIHIHIH

    }

    //qui per effettuare il logout voglio pure la password perchè non so come gestire il fatto che magari lui voglia disconnettere un altro utente dandomi solo il nickname
    //TODO match utente con il socket
    public String logout(String[] args){

        String username = args[1];
        String ret;

        hash_users obj = singleton_db.getInstanceUtenti();
        ret = obj.logout(username);
        return ret;
    }

    //TODO: String username = match con socket;
    public String createproject(String[] args){
        String projectname = args[1];
        String username;
        String ret;

        //TODO: prendere username dal socket
        hash_project obj = singleton_db.getInstanceProgetti();
        ret = obj.add_project(projectname,username);
        return ret;
    }

    public String addmember(String[] args){
        String projectname = args[1];
        String username = args[2];
        String ret;

        hash_project obj = singleton_db.getInstanceProgetti();
        ret = obj.add_projectmember(projectname, username);
        return ret;
    }

    public LinkedList<String> showmembers(String[] args){
        String projectname = args[1];

        hash_project obj = singleton_db.getInstanceProgetti();
        LinkedList<String> membri = obj.show_members(projectname);
        return membri;
    }

    




}
