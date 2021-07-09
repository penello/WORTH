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

    public static void main(String[] args){
        //creazione del clientmanager per la gestione di tutte le comunicazioni con il server
        ClientManager CM = new ClientManager();
        try {
            //creazione interfaccia grafica
            Main_GUI log = new Main_GUI(CM);
        } catch (Exception e) {
            // caso in cui il server sia offline
            JOptionPane.showMessageDialog(null, "Server offline!");
        }

    }



}
