import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
@SuppressWarnings("InfiniteLoopStatement")
public class ServerMain {

    private static CbServerImplementation server;
    public static void main(String[] args){

        //funzione per recuperare lo stato del servizio se ci sono dati salvati
        restoreBackup();

        try {
            //registrazione presso il registry per la callback
            server = new CbServerImplementation();
            String name = "Callback";
            LocateRegistry.createRegistry(2089);
            Registry registry = LocateRegistry.getRegistry (2089);
            registry.rebind (name, server);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        //avvio la connessione RMI per la fase di registrazione
        try{
            new Registration(server).start();
        }catch (Exception e){
            e.printStackTrace();
        }

        //avvio la connessione TCP
        try {
            ServerSocket listeningSocket = new ServerSocket();
            //il server resta in ascolto sulla porta 4569
            listeningSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 4685));
            ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
            while(true){
                //accetto le richieste di connessione da parte degli utenti
                Socket socket = listeningSocket.accept();
                //avvio un thread per client per gestire le loro richieste
                threadPool.execute(new Op_server(socket,server));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //funzione per recuperaro lo stato precedente del servizio se ci sono dati salvati presenti
    private static void restoreBackup() {
        try{
            File recoveryDir = new File(Persistent_data.getInstance().getProject_folder());
            for (File directory : recoveryDir.listFiles()) {
                if (directory.isDirectory()) {
                    String projectName = directory.getName();

                    if (!Singleton_db_progetti.getInstanceProgetti().restore_project(projectName)) return;

                    for (File file : directory.listFiles()) {

                        try (ObjectInputStream inputFile = new ObjectInputStream(
                                new FileInputStream(Persistent_data.getInstance().getProject_folder() + projectName + "/" + file.getName()))) {

                            if (file.getName().startsWith("membri")) {
                                LinkedList<String> tmp = new LinkedList<String>();
                                tmp = (LinkedList<String>) inputFile.readObject();
                                Singleton_db_progetti.getInstanceProgetti().restore_member(tmp, projectName);
                            } else {
                                Card carta = (Card) inputFile.readObject();
                                Singleton_db_progetti.getInstanceProgetti().restore_card(carta, projectName);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
