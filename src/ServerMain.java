import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
@SuppressWarnings("InfiniteLoopStatement")
public class ServerMain {
    public static void main(String[] args){


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
}
