import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

public class CbClientImplementation extends UnicastRemoteObject implements CbClientInterface {

    private ConcurrentHashMap<String,User> utenti;

    public CbClientImplementation(ConcurrentHashMap<String,User> utenti) throws RemoteException {
        super( );
        this.utenti = utenti;
    }

    /* metodo che può essere richiamato dal servente */
    public void notifyMe(User utente) throws RemoteException {
        if(utenti.putIfAbsent(utente.getUsername(),utente) != null){
            User tmp = utenti.get(utente.getUsername());
            if(utente.getStato().equals("online")){
                tmp.vai_off();
            }
            else tmp.vai_on();
        }
    }
}
