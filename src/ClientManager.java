import javax.swing.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ClientManager {

    private ConcurrentHashMap<String,User> utenti_registrati;
    private static int porta_tcp ;
    private static int porta_rmi_registry ;
    private static int porta_rmi_callback ;
    private String host;
    private Socket socket;
    private static Registration_interface registration;
    private CbClientImplementation callbackObj;
    private CbServerInterface server;
    private BufferedReader reader; // stream dal server TCP al client
    private BufferedWriter writer; // stream dal client TCP al server

    /**
     * costruttore della classe, setta i parametri di avvio
     * @param ip
     * @param tcp
     * @param rmi_r
     * @param rmi_c
     */
    public ClientManager(String ip, int tcp, int rmi_r, int rmi_c) {
        porta_tcp = tcp;
        porta_rmi_registry = rmi_r;
        porta_rmi_callback = rmi_c;
        host=ip;

        try {
            /**
             * Rmi per la registrazione
             * ottengo un riferimento all'oggetto remoto in modo da utilizzare i suoi metodi
             */
            Registry registration_registry = LocateRegistry.getRegistry(porta_rmi_registry);
            /**
             * recupero la registry sulla porta RMI
             */
            registration = (Registration_interface) registration_registry.lookup("Sign in");
        }catch (RemoteException e){
            JOptionPane.showMessageDialog(null, "Server error:" + e.getMessage());
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Server error:" + e.getMessage());
        }
        try {
            /**
             * Rmi callback per le notifiche
             * esporto l'oggetto con i metodi che può usare il server
             */
            callbackObj = new CbClientImplementation(utenti_registrati);
            /**
             * recupero la registry sulla porta Rmi
             */
            Registry registry = LocateRegistry.getRegistry(porta_rmi_callback);
            /**
             * richiedo l'oggetto dal nome pubblico
             */
            server = (CbServerInterface) registry.lookup("Callback");
        } catch (RemoteException e){
            JOptionPane.showMessageDialog(null, "Server error:" + e.getMessage());
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Server error:" + e.getMessage());
        }
        try {
            /**
             * Connessione TCP con il server
             */
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, porta_tcp));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e){
            JOptionPane.showMessageDialog(null, "Server error:" + e.getMessage());
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Server error:" + e.getMessage());
        }

    }

    /**
     * metodo usato per inviare un messaggio sulla connessione TCP al server
     * @param msg
     * @throws IOException
     */
    private void send_message(String msg) throws IOException{
        writer.write(msg + "\n");
        writer.flush();
    }

    /**
     * metodo usato per leggere un messaggio sulla connessione TCP proveniente dal server
     * @return
     * @throws IOException
     */
    private String read_message() throws IOException{
        String result;
        result = reader.readLine();
        return result;
    }

    /**
     * metodo per registrarsi al servizio
     * @param username
     * @param password
     * @return il risultato dll'operazione
     * @throws RemoteException
     */
    public String register(String username, String password) throws RemoteException{
        String result = null;
        result = registration.register(username, password);
        return result;
    }

    /**
     * metodo per loggarsi al servizio
     * @param username
     * @param password
     * @return il risultato dell'operazione
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public String login(String username, String password) throws IOException , ClassNotFoundException{
        if(username == null || password == null) throw new NullPointerException();
        send_message("login " + username + " " + password);
        String risposta = read_message();
        if(risposta.startsWith("SUCCESS")){
            server.RegisterForCallback(callbackObj);
            //aggiorno la struttura dati locale con quella aggiornata passata con la callback
            this.utenti_registrati = callbackObj.gethashmap();
        }
        return risposta;
    }

    /**
     * metodo per reperire dal server l'ip per la chat del progetto
     * @param projectname
     * @return il risultato dell'operazione
     * @throws IOException
     */
    public String get_chat_multicast(String projectname)throws IOException{
        if(projectname == null) throw new NullPointerException();
        send_message("getchat " + projectname);
        String risposta = read_message();
        if(!risposta.isEmpty()) return risposta;
        else return "Errore nel reperimento dell'ip muticast";
    }

    /**
     * metodo per eseguire il logout dal servizio
     * @param username
     * @return il risultato dell'operazione
     * @throws IOException
     */
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

    /**
     * metodo per far termirare il client in maniera pulita
     * @throws RemoteException
     */
    public void close() throws RemoteException {
        UnicastRemoteObject.unexportObject(callbackObj, false);
        if (server != null)
            server.UnregisterForCallback(callbackObj);
    }

    /**
     * metodo usato per avere la lista dei progetti di cui l'utente è membro
     * @param username
     * @return il risultato dell'operazione
     * @throws IOException
     */
    public String listProjects(String username) throws IOException {
        if(username == null) throw new NullPointerException();
        send_message("listproject " + username);
        String risposta = read_message();
        return risposta;
    }

    /**
     * metodo usato per richiedere la creazione di un progetto
     * @param projectname
     * @return il risultato dell'operazione
     * @throws IOException
     */
    public String createproject(String projectname) throws IOException {
        if(projectname == null) throw new NullPointerException();
        send_message("createproject " + projectname);
        String risposta = read_message();
        return risposta;
    }

    /**
     * metodo usato per richiedere l'aggiunta di un membro al progetto
     * @param projectname
     * @param nickutente
     * @return il risultato dell'operazione
     * @throws IOException
     */
    public String addmember(String projectname, String nickutente) throws IOException {
        if(projectname == null || nickutente == null) throw new NullPointerException();
        send_message("addmember " + projectname + " " + nickutente);
        String risposta = read_message();
        return risposta;
    }

    /**
     * metodo usato per richiedere la lista dei membri del progetto
     * @param projectname
     * @return
     * @throws IOException
     */
    public String showmembers(String projectname) throws IOException {
        if(projectname == null) throw new NullPointerException();
        send_message("showmembers " + projectname);
        String risposta = read_message();
        return risposta;
    }

    /**
     * metodo usato per richiedere la carte presenti nel progetto
     * @param projectname
     * @return il risultato dell'operazione
     * @throws IOException
     */
    public String showcards(String projectname) throws IOException {
        if(projectname == null) throw new NullPointerException();
        send_message("showcards " + projectname);
        String risposta = read_message();
        return risposta;

    }

    /**
     * metodo usato per richiedere una specifica carta del progetto e le sue informazioni
     * @param projectName
     * @param cardName
     * @return il risultato dell'operazione
     * @throws IOException
     */
    public String showcard(String projectName, String cardName) throws IOException {
        if(projectName == null || cardName == null) throw new NullPointerException();
        send_message("showcard " + projectName + " " + cardName);
        String risposta = read_message();
        return risposta;
    }

    /**
     * metodo usato per richiedere l'aggiunta di una card al progetto
     * @param projectName
     * @param cardName
     * @param descrizione
     * @return il risultato dell'operazione
     * @throws IOException
     */
    public String addCard(String projectName, String cardName, String descrizione) throws IOException {
        if(projectName == null || cardName == null || descrizione == null) throw new NullPointerException();
        send_message("addcard " + projectName + " " + cardName + " " + descrizione);
        String risposta = read_message();
        return risposta;

    }

    /**
     * metodo usato per richiedere lo sppstamento di una carta del progetto
     * @param projectName
     * @param cardName
     * @param listaPartenza
     * @param listaDestinazione
     * @return il risultato dell'operazione
     * @throws IOException
     */
    public String moveCard(String projectName,String cardName,String listaPartenza,String listaDestinazione) throws IOException {
        if(projectName == null || cardName == null || listaPartenza == null || listaDestinazione == null) throw new NullPointerException();
        send_message("movecard " + projectName + " " + cardName + " " + listaPartenza + " " + listaDestinazione);
        String risposta = read_message();
        return risposta;
    }

    /**
     * metodo usato per richiedere la lista dei movimenti di una carta del progetto
     * @param projectName
     * @param cardName
     * @return il risultato dell'operazione
     * @throws IOException
     */
    public String getCardHistory(String projectName, String cardName) throws IOException {
        if(projectName == null || cardName == null) throw new NullPointerException();
        send_message("getcardhistory " + projectName + " " + cardName);
        String risposta = read_message();
        return risposta;
    }

    /**
     * metodo usato per richiedere la cancellazione di un progetto
     * @param projectName
     * @return il risultato dell'operazione
     * @throws IOException
     */
    public String cancelProject(String projectName) throws IOException {
        if( projectName == null) throw new NullPointerException();
            send_message("cancelproject " + projectName);
            String risposta = read_message();
            return risposta;
    }

    /**
     * metodo utilizzato per visualizzare la lista degli utenti registrati e il loro stato
     * @return il risultato dell'operazione
     */
    public String listUsersFunction(){
        String result = "List of all users: ";
        AtomicReference<String> str = new AtomicReference<>(" ");
        utenti_registrati.forEach((k,v)->{
            str.set(str + k + " stato: " + v.getStato() +"|");
        });
        result = result + str.toString();
        return result;
    }

    /**
     * metodo utilizzato per visualizzare la lista degli utenti online
     * @return il risultato dell'operazione
     */
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