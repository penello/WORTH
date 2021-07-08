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
    public static void main(String[] args){

        restoreBackup();

        try { //registrazione presso il registry
            CbServerImplementation server = new CbServerImplementation();
            String name = "Callback";
            LocateRegistry.createRegistry(2089);
            Registry registry = LocateRegistry.getRegistry (2089);
            registry.bind (name, server);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }


        try{
            new Registration().start();
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            ServerSocket listeningSocket = new ServerSocket();
            listeningSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 4685));      //il server resta in ascolto sulla porta 4569

            ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();   //creo un threadpool
            while(true){
                Socket socket = listeningSocket.accept();       //accetto le richieste di connessione da parte degli utenti
                System.out.println("System: un utente si e' connesso al sistema");
                threadPool.execute(new Op_server(socket));   //gestisco le loro richieste
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
