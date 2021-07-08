import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CbServerImplementation extends UnicastRemoteObject implements CbServerInterface {

    //lista client registrati
    private List<CbClientInterface> clients;

    public CbServerImplementation() throws RemoteException{
        super();
        clients = new ArrayList<CbClientInterface>();
    }

    public synchronized void RegisterForCallback(CbClientInterface callbackClient) throws RemoteException{
        if (!clients.contains(callbackClient)) { clients.add(callbackClient); }
    }
    /* annulla registrazione per il callback */
    public synchronized void UnregisterForCallback(CbClientInterface callbackClient) throws RemoteException{
        clients.remove(callbackClient);
    }
    /* metodo di notifica
     * quando viene richiamato, fa il callback a tutti i client registrati */
    public void notifica (User utente) throws RemoteException {
        doCallbacks(utente);
    }
    private synchronized void doCallbacks(User utente) throws RemoteException{
        Iterator<CbClientInterface> i = clients.iterator( );
        while (i.hasNext()) {
            CbClientInterface client = i.next();
            client.notifyMe(utente);
        }
    }
}
