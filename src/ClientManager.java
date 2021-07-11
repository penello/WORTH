import javax.swing.*;
import java.io.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ClientManager {

    private ConcurrentHashMap<String,User> utenti_registrati;
    private boolean log_in;
    private String Username;
    private static int porta_tcp = 4300;
    private static int porta_rmi_registry = 4201;
    private static int porta_rmi_callback = 4202;
    private Socket socket;
    private DatagramSocket socketudp;
    private static Registration_interface registration;
    private CbClientImplementation callbackObj;
    private CbServerInterface server;
    private BufferedReader reader; // stream dal server TCP al client
    private BufferedWriter writer; // stream dal client TCP al server


    public ClientManager() {
        try {
            // RMI- ottengo un riferimento all'oggetto remoto in modo da utilizzare i suoi metodi
            Registry registration_registry = LocateRegistry.getRegistry(porta_rmi_registry);
            // recupero la registry sulla porta RMI
            registration = (Registration_interface) registration_registry.lookup("Sign in");
        }catch (RemoteException e){
            JOptionPane.showMessageDialog(null, "Server error:" + e.getMessage());
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Server error:" + e.getMessage());

        }
        try {
            // Callbacks RMI
            //Creo l'oggetto esportato con i metodi che può usare il server per aggiornare la struttura dati
            callbackObj = new CbClientImplementation(utenti_registrati);
            // recupero la registry sulla porta RMI
            Registry registry = LocateRegistry.getRegistry(porta_rmi_callback);
            // richiedo l'oggetto dal nome pubblico
            server = (CbServerInterface) registry.lookup("Callback");
        } catch (RemoteException e){
            JOptionPane.showMessageDialog(null, "Server error:" + e.getMessage());
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Server error:" + e.getMessage());
        }
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", porta_tcp));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "Server error:" + e.getMessage());
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Server error:" + e.getMessage());
        }

    }

    private void send_message(String msg) throws IOException{
        writer.write(msg + "\n");
        writer.flush();
    }

    private String read_message() throws IOException{
        String result;
        result = reader.readLine();
        return result;
    }

    public String register(String username, String password) throws RemoteException{
        String result = null;
        result = registration.register(username, password);
        return result;
    }

    public String login(String username, String password) throws IOException , ClassNotFoundException{
        if(username == null || password == null) throw new NullPointerException();
        send_message("login " + username + " " + password);
        String risposta = read_message();
        if(risposta.startsWith("SUCCESS")){
            //aggiorno la struttura dati locale con quella aggiornata passata con la callback
            server.RegisterForCallback(callbackObj);
            this.utenti_registrati = callbackObj.gethashmap();
        }
        this.Username = username;
        return risposta;
    }

    public String get_chat_multicast(String projectname)throws IOException{
        if(projectname == null) throw new NullPointerException();
        send_message("getchat " + projectname);
        String risposta = read_message();
        if(!risposta.isEmpty()) return risposta;
        else return "Errore nel reperimento dell'ip muticast";
    }

    public String logout(String username) throws IOException{
        if(username == null) throw new NullPointerException();
        send_message("logout " + username);
        String risposta = read_message();
        if(risposta.startsWith("SUCCESS")){
            server.UnregisterForCallback(callbackObj);
            this.utenti_registrati.clear();
        }
        return risposta;
    }

    public void close() throws RemoteException {
        //il client termina in maniera pulita se faccio close
        UnicastRemoteObject.unexportObject(callbackObj, false);
        if (server != null)
            server.UnregisterForCallback(callbackObj);
    }

    public String listProjects(String username) throws IOException {
        if(username == null) throw new NullPointerException();
        send_message("listproject " + username);
        String risposta = read_message();
        return risposta;
    }

    public String createproject(String projectname) throws IOException {
        if(projectname == null) throw new NullPointerException();
        send_message("createproject " + projectname);
        String risposta = read_message();
        return risposta;
    }

    public String addmember(String projectname, String nickutente) throws IOException {
        if(projectname == null || nickutente == null) throw new NullPointerException();
        send_message("addmember " + projectname + " " + nickutente);
        String risposta = read_message();
        return risposta;
    }

    public String showmembers(String projectname) throws IOException {
        if(projectname == null) throw new NullPointerException();
        send_message("showmembers " + projectname);
        String risposta = read_message();
        return risposta;
    }

    public String showcards(String projectname) throws IOException {
        if(projectname == null) throw new NullPointerException();
        send_message("showcards " + projectname);
        String risposta = read_message();
        return risposta;

    }

    public String showcard(String projectName, String cardName) throws IOException {
        if(projectName == null || cardName == null) throw new NullPointerException();
        send_message("showcard " + projectName + " " + cardName);
        String risposta = read_message();
        return risposta;
    }

    public String addCard(String projectName, String cardName, String descrizione) throws IOException {
        if(projectName == null || cardName == null || descrizione == null) throw new NullPointerException();
        send_message("addcard " + projectName + " " + cardName + " " + descrizione);
        String risposta = read_message();
        return risposta;

    }


    public String moveCard(String projectName,String cardName,String listaPartenza,String listaDestinazione) throws IOException {
        if(projectName == null || cardName == null || listaPartenza == null || listaDestinazione == null) throw new NullPointerException();
        send_message("movecard " + projectName + " " + cardName + " " + listaPartenza + " " + listaDestinazione);
        String risposta = read_message();
        return risposta;
    }

    public String getCardHistory(String projectName, String cardName) throws IOException {
        if(projectName == null || cardName == null) throw new NullPointerException();
        send_message("getcardhistory " + projectName + " " + cardName);
        String risposta = read_message();
        return risposta;
    }

    public String cancelProject(String projectName) throws IOException {
        if( projectName == null) throw new NullPointerException();
            send_message("cancelproject " + projectName);
            String risposta = read_message();
            return risposta;
    }

    public String listUsersFunction(){
        String result = "List of all users: ";
        AtomicReference<String> str = new AtomicReference<>(" ");
        utenti_registrati.forEach((k,v)->{
            str.set(str + k +"|");
        });
        result = result + str.toString();
        return result;
    }
    public String listOnlineUsersFunction() {
        String result = "Online users: ";
        AtomicReference<String> str = new AtomicReference<>(" ");
        utenti_registrati.forEach((k, v) -> {
            if ((v.getStato()).equals("online")) {
                str.set(str + k + "|");
            }
        });
        result = result + str.toString();
        return result;
    }
}