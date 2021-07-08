import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CbServerInterface extends Remote {

    //metodo di notifica
    public void notifica(User utente) throws RemoteException;

    //metodo per registrarsi alle callback
    public void RegisterForCallback(CbClientInterface callbackClient) throws RemoteException, IOException, ClassNotFoundException;

    //metodo per deregistrarsi alle callback
    public void UnregisterForCallback(CbClientInterface callbackClient) throws RemoteException;
}
