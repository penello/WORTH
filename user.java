import java.io.Serializable;
import java.util.ArrayList;

public class user implements Serializable {

    private String username;
    private String password;
    private String stato;
    private ArrayList<project> lista_progetti;

    public user(String nick, String psswd){
        //TODO: SHA256 di password
        username = nick;
        password = psswd;
        stato = "offline";
        lista_progetti = new ArrayList<project>();
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
        //TODO: equals dello SHA256 di password
        return password.equals(this.password);
    }
}
