import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
@SuppressWarnings("InfiniteLoopStatement")
public class ServerMain {
    public static void main(String[] args){

        restoreBackup();

        new registration().start();

        try {
            ServerSocket listeningSocket = new ServerSocket();
            listeningSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 4685));      //il server resta in ascolto sulla porta 4569

            ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();   //creo un threadpool
            while(true){
                Socket socket = listeningSocket.accept();       //accetto le richieste di connessione da parte degli utenti
                System.out.println("System: un utente si e' connesso al sistema");
                threadPool.execute(new op_server(socket));   //gestisco le loro richieste
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void restoreBackup() {

        File recoveryDir = new File(persistent_data.getInstance().getProject_folder());

        for (File directory : Objects.requireNonNull(recoveryDir.listFiles())) {
            if (directory.isDirectory()) {
                String projectName = directory.getName();

                if(!singleton_db_progetti.getInstanceProgetti().restore_project(projectName)) return;

                for (File file : Objects.requireNonNull(directory.listFiles())) {

                    try (ObjectInputStream inputFile = new ObjectInputStream(
                            new FileInputStream(persistent_data.getInstance().getProject_folder() + projectName + "/" + file.getName()))) {

                        if (file.getName().startsWith("membri")) {
                            LinkedList<String> tmp = new LinkedList<String>();
                            tmp = (LinkedList<String>) inputFile.readObject();
                            singleton_db_progetti.getInstanceProgetti().restore_member(tmp,projectName);
                        } else {
                            card carta = (card) inputFile.readObject();
                            singleton_db_progetti.getInstanceProgetti().restore_card(carta,projectName);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
