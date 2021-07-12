import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ConcurrentHashMap;

public class CbClientImplementation extends UnicastRemoteObject implements CbClientInterface {

    private ConcurrentHashMap<String,User> utenti;

    /**
     *
     * @param utenti struttura dati contenente gli utenti
     * @throws RemoteException se fallisce l'esportazione dell'oggetto
     */
    public CbClientImplementation(ConcurrentHashMap<String,User> utenti) throws RemoteException {
        super( );
        this.utenti = utenti;
    }

    /**
     * metodo chiamato dal server per notificare l'utente di una modifica
     * @param utente
     */
    public void notifyMe(User utente) {
        utenti.remove(utente.getUsername());
        utenti.put(utente.getUsername(),utente);
    }

    /**
     * metodo chiamato dal server quando un utente effettua il login, viene passata la struttura dati contenente le informazioni degli utenti
     * @param utenti
     * @throws IOException
     * @throws ClassNotFoundException Class of a serialized object cannot be found
     */
    public void notifyUtenti(byte[] utenti) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bytein = new ByteArrayInputStream(utenti);
        ObjectInputStream instream = new ObjectInputStream(bytein);
        ConcurrentHashMap<String,User> tmp = (ConcurrentHashMap<String,User>) instream.readObject();
        this.utenti = tmp;
    }


    public ConcurrentHashMap<String,User> gethashmap(){return this.utenti;}
}