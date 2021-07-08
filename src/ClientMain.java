import javax.swing.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ClientMain {

    public ConcurrentHashMap<String,User> utenti_registrati;
    public ClientMain(){
        this.utenti_registrati = new ConcurrentHashMap<String,User>();
    }


    private static boolean  log_in;
    private static boolean ok = true;
    private static Registration_interface registration;


    public static void main(String[] args){

        ClientMain client = new ClientMain();
        JFrame frame = new JFrame("worth");
        Main_GUI main_gui = new Main_GUI(frame);
        String message = null;
        String result = null;
        log_in = false;

        BufferedReader reader; // stream dal server TCP al client
        BufferedWriter writer; // stream dal client TCP al server

        System.out.println("Welcome in WORTH");
        System.out.println("Please login or register to proceed.");



        try (Socket socket = new Socket();
             BufferedReader cmd_line = new BufferedReader(new InputStreamReader(System.in));) {

            // RMI- ottengo un riferimento all'oggetto remoto in modo da utilizzare i suoi metodi
            // recupero la registry sulla porta RMI
            Registry registration_registry = LocateRegistry.getRegistry(2048);
            // richiedo l'oggetto dal nome pubblico
            registration = (Registration_interface) registration_registry.lookup("Sign in");

            // Callbacks RMI
            //Creo l'oggetto esportato con i metodi che può usare il server per aggiornare la struttura dati
            CbClientImplementation callbackObj = new CbClientImplementation(client.utenti_registrati);
            // recupero la registry sulla porta RMI
            Registry registry = LocateRegistry.getRegistry(2089);
            // richiedo l'oggetto dal nome pubblico
            CbServerInterface server = (CbServerInterface) registry.lookup("Callback");

            // TCP Connection
            socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 4685));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));


            while (ok) {
                System.out.println();
                System.out.printf("> ");

                try {
                    message = cmd_line.readLine();
                    String[] myArgs = message.split(" ");

                    if (message.startsWith("close")) {
                        ok = false;
                        break;
                    }
                    if(!message.startsWith("login") && !log_in){
                        System.out.println("Effettuare il login prima di ogni altra operazione");
                        continue;
                    }
                    else {
                        //operazione per registrarsi al servizio
                        if (message.startsWith("register")) {
                            registerFunction(myArgs);
                            continue;
                            //operazione per visualizzare la lista degli utenti registrati al servizio
                        } else if (message.startsWith("listUsers")) {
                            client.listUsersFunction();
                            continue;
                            //operazione per visualizzare la lista degli utenti registrati al servizio e in stato online
                        } else if (message.startsWith("listOnlineUsers")) {
                            client.listOnlineUsersFunction();
                            continue;
                        }
                    }

                    writer.write(message + "\r\n");
                    writer.flush();
                    while (!(result = reader.readLine()).isEmpty()) {
                        if (message.startsWith("login") && result.startsWith("SUCCESS")) {
                            log_in = true;
                            server.RegisterForCallback(callbackObj);
                            //aggiorno la struttura dati locale con quella aggiornata passata con la callback
                            client.utenti_registrati = callbackObj.gethashmap();
                        } else if (message.startsWith("logout") && result.startsWith("SUCCESS")) {
                            server.UnregisterForCallback(callbackObj);
                            log_in = false;
                            client.utenti_registrati.clear();
                        }
                        System.out.println("< " + result);
                    }
                } catch (Exception e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }

            //il client termina in maniera pulita
            UnicastRemoteObject.unexportObject(callbackObj, false);
            if (server != null)
                server.UnregisterForCallback(callbackObj);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerFunction(String[] myArgs) throws RemoteException {
        String result;
        if(log_in)
            result = "Error. Log out before new registration";
        else
            result = registration.register(myArgs[1],myArgs[2]);
        System.out.println("< "+result);
    }

    public void listUsersFunction(){
        String result = "List of all users";
        System.out.println("< "+result);

        AtomicReference<String> str = new AtomicReference<>(" ");
        utenti_registrati.forEach((k,v)->{
            str.set(str + k +"|");
        });
        result = str.toString();
        System.out.println(result);
        System.out.flush();
    }

    private void listOnlineUsersFunction() {
        String result = "Online users";
        System.out.println("< "+result);

        AtomicReference<String> str = new AtomicReference<>(" ");
        utenti_registrati.forEach((k,v)->{
            if((v.getStato()).equals("online")){
                str.set(str + k +"|");
            }
        });
        result = str.toString();
        System.out.println(result);
        System.out.flush();
    }

}
