import java.io.Serializable;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class Hash_users implements Serializable {

    private ConcurrentHashMap<String, User> utenti;

    public Hash_users(){
        //TODO: add initialCapacity and concurrencyLevel
        //utenti = new ConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel);
        utenti = new ConcurrentHashMap<>();
    }

    public User get_user(String username){
        return utenti.get(username);
    }

    public String add_user(String username, String password){
        User new_user = new User(username, password);
        if(utenti.putIfAbsent(username, new_user) != null) return "l'utente " + username + " è già registrato";
        //TODO: serializzare e notificare la creazione di un nuovo utente
        return "SUCCESS l'utente "+username+" è stato registrato correttamente";
    }


    public boolean member(String username){
        return utenti.containsKey(username);
    }

    public boolean login(String username, String password){

        User ut = utenti.get(username);

        if(ut == null) return false;
        else{
            if(!ut.verify_password(password)) return false;
            ut.vai_on();
            //TODO:notificare la modifica
        }
        return true;
    }

    public String getUsers(){
        String ret = " ";
        AtomicReference<String> str = new AtomicReference<>(" ");
        utenti.forEach((k,v)->{
            str.set(str + k +"|");
        });
        ret = str.toString();
        return ret;
    }

    //TODO: fare il logout anche se cade connessione
    public String logout(String username){

        User ut = utenti.get(username);
        ut.vai_off();
        //TODO:notificare la modifica
        return "SUCCESS";

    }

    public String get_onlineusers(){
        String ret = "SUCCESS ";
        AtomicReference<String> str = new AtomicReference<>(" ");
        utenti.forEach((k,v)->{
            if((v.getStato()).equals("online")){
                str.set(str + k +"|");
            }
        });
        ret = str.toString();
        return ret;
    }

    public String get_listproject(String username){
        User utente = utenti.get(username);
        if(utente == null) return "Utente inesistente, prego registrarsi";
        String s = "SUCCESS Lista dei progetti di cui l'utente è membro: ";

        Iterator<String> iterator = (utente.getLista_progetti()).iterator();
        while(iterator.hasNext()){
            s = s+iterator.next()+"|";
        }
        return s;
    }

}
