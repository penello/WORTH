import javax.swing.*;
import java.io.*;
import java.util.Properties;

public class ClientMain {

    /**
     * metodo usato per caricare i parametri di avvio del client, quali la porta tcp, ip del server e le porte rmi
     * @param path percorso del file di configurazione
     * @return
     */
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

    /**
     * main del client
     * @param args
     */
    public static void main(String[] args){
        Properties properties = load_properties("client.ini");

        String server_address = properties.getProperty("server_ip");
        int tcp = Integer.parseInt(properties.getProperty("porta_tcp"));
        int rmi_r = Integer.parseInt(properties.getProperty("porta_rmi"));
        int rmi_c = Integer.parseInt(properties.getProperty("porta_rmicallback"));

        //creazione del clientmanager per la gestione di tutte le comunicazioni con il server
        ClientManager CM = new ClientManager(server_address,tcp,rmi_r,rmi_c);
        try {
            //creazione interfaccia grafica
            Main_GUI log = new Main_GUI(CM);
        } catch (Exception e) {
            // caso in cui il server sia offline
            JOptionPane.showMessageDialog(null, "Server offline!");
        }
    }
}
