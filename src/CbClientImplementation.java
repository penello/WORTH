import javax.imageio.IIOException;
import java.io.*;
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
        //se esiste vuol dire che la chiamata è stata fatta per un cambio di stato dell'utente
        if(utenti.putIfAbsent(utente.getUsername(),utente) != null){
            User tmp = utenti.get(utente.getUsername());
            if(utente.getStato().equals("online")){
                tmp.vai_on();
            }
            else tmp.vai_off();
        }
    }

    public void notifyUtenti(byte[] utenti) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bytein = new ByteArrayInputStream(utenti);
        ObjectInputStream instream = new ObjectInputStream(bytein);
        ConcurrentHashMap<String,User> tmp = (ConcurrentHashMap<String,User>) instream.readObject();
        this.utenti = tmp;
    }

    public ConcurrentHashMap<String,User> gethashmap(){return this.utenti;}
}