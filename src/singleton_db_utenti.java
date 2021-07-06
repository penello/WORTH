import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.Collection;

public class singleton_db_utenti {

    private volatile static singleton_db_utenti istanza = null;

    private hash_users utenti;

    private singleton_db_utenti() {
        try (ObjectInputStream input = new ObjectInputStream(
                new FileInputStream(persistent_data.getInstance().getUser_folder()));) {
            utenti = (hash_users) input.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("System: no users registred");
            utenti = new hash_users();
        } catch (Exception e) {
            utenti = new hash_users();
        }

    }

    public static hash_users getInstanceUtenti(){
        if(istanza == null){
            synchronized (singleton_db_utenti.class){
                //non sono sicuro di essere il primo thread ad aver preso la lock quindi ricontrollo istanza
                if(istanza == null){

                    istanza = new singleton_db_utenti();
                }
            }
        }
        return istanza.utenti;
    }

}