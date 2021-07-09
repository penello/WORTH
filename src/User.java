//import org.apache.commons.codec.digest.DigestUtils;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

    private String username;
    private String password;
    private String stato;
    private ArrayList<String> lista_progetti;

    public User(String nick, String psswd){
        //TODO: SHA256 di password, controllare se possso farlo senza la libreria pazza e nel caso non posso riferire nella relazione
        //String sha256hex = DigestUtils.sha256Hex(psswd);
        username = nick;
        //password = sha256hex;
        password = psswd;
        stato = "offline";
        lista_progetti = new ArrayList<String>();
    }

    public synchronized String getUsername(){
        return username;
    }

    public synchronized String getPassword(){
        return password;
    }

    public synchronized String getStato(){
        return stato;
    }

    public synchronized ArrayList<String> getLista_progetti() {return lista_progetti;}

    public synchronized void vai_on(){
        if (stato.equals("online")){
            //TODO: raise Exception
        }
        stato = "online";
    }

    public synchronized void vai_off(){
        if(stato.equals("offline")){
            //TODO: raise exception
        }
        stato = "offline";
    }

    public synchronized boolean verify_password(String password){
        //TODO: equals dello SHA256 di password dirlo alla proff
        //String sha256hex = DigestUtils.sha256Hex(password);
        //return sha256hex.equals(this.password);
        return password.equals(this.password);
    }

    public boolean add_project(String projectname){
        return lista_progetti.add(projectname);
    }

    public boolean cancel_project (String projectname) { return lista_progetti.remove(projectname);}
}