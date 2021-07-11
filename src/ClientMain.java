import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ClientMain {

    private static Properties load_properties(String path){
        Properties properties = new Properties();

        properties.setProperty("porta_rmi", "4201");
        properties.setProperty("porta_tcp", "4300");
        properties.setProperty("porta_rmicallback", "4202");
        properties.setProperty("server_ip", "127.0.0.1");

        try(FileReader fileReader = new FileReader(path)){
            properties.load(fileReader);

        } catch (FileNotFoundException e){
            try(FileWriter output = new FileWriter(path)){
                properties.store(output, "WORTH CLIENT PROPERTIES");
            } catch (IOException e2) {
                e.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return properties;
    }

    public static void main(String[] args){
        Properties properties = load_properties("client.ini");
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
