import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CbClientInterface extends Remote {

    /* Metodo invocato dal server per effettuare una callback a un client remoto. */
    public void notifyMe(User utente) throws RemoteException;
}
