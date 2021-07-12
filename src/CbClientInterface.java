import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CbClientInterface extends Remote {

    /**
     * metodo invocato dal server per effettuare una callback a un client remoto.
     * @param utente
     * @throws RemoteException
     */
    public void notifyMe(User utente) throws RemoteException;

    /**
     * metodo invocato dal server per effettuare una callback a un client remoto
     * @param byteout
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void notifyUtenti(byte[] byteout) throws IOException, ClassNotFoundException;
}
