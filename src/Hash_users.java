import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class Hash_users implements Serializable {

    private ConcurrentHashMap<String, User> utenti;

    public Hash_users(){
        utenti = new ConcurrentHashMap<>();
    }

    /**
     *
     * @param username
     * @return l'utente con come username
     */
    public User get_user(String username){
        return utenti.get(username);
    }

    /**
     *
     * @return la struttura dati contenente gli utenti
     */
    public ConcurrentHashMap<String, User> get_concurrent_hashmap(){
        return utenti;
    }

    /**
     * metodo usato per aggiungere un nuovo utente alla struttura dati
     * @param username
     * @param password
     * @return il risultato dell'operazione
     */
    public String add_user(String username, String password){
        User new_user = new User(username, password);
        if(utenti.putIfAbsent(username, new_user) != null) return "l'utente " + username + " è già registrato";
        return "SUCCESS";
    }

    /**
     * metodo usato per effettuare il login di un utente
     * @param username
     * @param password
     * @return il risultato dell'operazione
     */
    public boolean login(String username, String password){

        User ut = utenti.get(username);

        if(ut == null) return false;
        else{
            if(!ut.verify_password(password)) return false;
            ut.vai_on();
        }
        return true;
    }

    /**
     *
     * @return una stringa contenente gli utenti registrati
     */
    public String getUsers(){
        String ret = " ";
        AtomicReference<String> str = new AtomicReference<>(" ");
        utenti.forEach((k,v)->{
            str.set(str + k +"|");
        });
        ret = str.toString();
        return ret;
    }

    /**
     * metodo usato per effettuare il logout di un utente
     * @param username
     * @return il risultato dell'operazione
     */
    public String logout(String username){

        User ut = utenti.get(username);
        ut.vai_off();
        return "SUCCESS";
    }

    /**
     * metodo usato per recuperare la lista dei progetti di cui l'utente è membro
     * @param username
     * @return il risultato dell'operazione
     */
    public String get_listproject(String username){
        User utente = utenti.get(username);
        if(utente == null) return "Utente inesistente, prego registrarsi";
        String s = "SUCCESS ";
        ArrayList<String> tmp = utente.getLista_progetti();
        Iterator<String> iterator = (tmp.iterator());
        while(iterator.hasNext()){
            s = s+iterator.next()+"|";
        }
        return s;
    }
}
