import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;

public class Singleton_db_utenti {

    private volatile static Singleton_db_utenti istanza = null;

    private Hash_users utenti;

    /**
     * costruttore che recupera i dati persistenti se sono presenti
     */
    private Singleton_db_utenti() {
        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(Persistent_data.getInstance().getUser_folder()+"utenti.json"));) {
            utenti = (Hash_users) input.readObject();
            System.out.println("ok utenti ripresi");
        } catch (FileNotFoundException e) {
            System.out.println("System: no users registred");
            utenti = new Hash_users();
        } catch (Exception e) {
            utenti = new Hash_users();
        }

    }

    public static Hash_users getInstanceUtenti(){
        if(istanza == null){
            synchronized (Singleton_db_utenti.class){
                //non sono sicuro di essere il primo thread ad aver preso la lock quindi ricontrollo istanza
                if(istanza == null){

                    istanza = new Singleton_db_utenti();
                }
            }
        }
        return istanza.utenti;
    }
}