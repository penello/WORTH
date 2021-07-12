import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CbServerInterface extends Remote {

    /**
     * metodo di notifica
     */
    public void notifica(User utente) throws RemoteException;

    /**
     * metodo per registrarsi alle callback
     * @param callbackClient
     * @throws RemoteException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void RegisterForCallback(CbClientInterface callbackClient) throws RemoteException, IOException, ClassNotFoundException;

    /**
     * metodo per deregistrarsi alle callback
     * @param callbackClient
     * @throws RemoteException
     */
    public void UnregisterForCallback(CbClientInterface callbackClient) throws RemoteException;
}
