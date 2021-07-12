import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;

public class User implements Serializable {

    private String username;
    private String password;
    private String stato;
    private ArrayList<String> lista_progetti;

    public User(String nick, String psswd){
        username = nick;
        password = psswd;
        stato = "offline";
        lista_progetti = new ArrayList<String>();
    }

    public synchronized String getUsername(){
        return username;
    }

    public synchronized String getStato(){
        return stato;
    }

    public synchronized ArrayList<String> getLista_progetti() {return lista_progetti;}

    public synchronized void vai_on(){
        stato = "online";
    }

    public synchronized void vai_off(){
        stato = "offline";
    }

    /**
     *
     * @param password
     * @return se la password coincide o meno
     */
    public synchronized boolean verify_password(String password){

        return password.equals(this.password);
    }

    /**
     * aggiunge un progetto alla lista dei progetti di un utente
     * @param projectname
     * @return il risultato dell'operazione
     */
    public boolean add_project(String projectname){
        return lista_progetti.add(projectname);
    }

    /**
     * cancella un progetto dalla lista dei progetti di un utente
     * @param projectname
     * @return il risultato dell'operazione
     */
    public boolean cancel_project (String projectname) { return lista_progetti.remove(projectname);}
}