import javax.imageio.IIOException;
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class CbServerImplementation extends UnicastRemoteObject implements CbServerInterface {

    //lista client registrati
    private List<CbClientInterface> clients;

    public CbServerImplementation() throws RemoteException{
        super();
        clients = new ArrayList<CbClientInterface>();
    }

    public synchronized void RegisterForCallback(CbClientInterface callbackClient) throws RemoteException, IOException, ClassNotFoundException {
        if (!clients.contains(callbackClient)) {
            clients.add(callbackClient);
            ConcurrentHashMap<String, User> utenti = Singleton_db_utenti.getInstanceUtenti().get_concurrent_hashmap();
            ByteArrayOutputStream byteout = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(byteout);
            output.writeObject(utenti);
            output.flush();
            output.close();
            byteout.close();
            byte b[] = byteout.toByteArray();
            callbackClient.notifyUtenti(b);
        }
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
        Iterator<CbClientInterface> i = clients.iterator();
        LinkedList<CbClientInterface> toRemove = new LinkedList<CbClientInterface>() ;
        while (i.hasNext()) {
            CbClientInterface client = (CbClientInterface) i.next();
            try{
                client.notifyMe(utente);
            }
            catch (IOException e){
                toRemove.add(client);
            }
        }

        //Rimuovo i client che mi hanno lanciato una eccezione
        //Così evito di controllarli nuovamente
        i = toRemove.iterator();
        while (i.hasNext()){
            CbClientInterface client = (CbClientInterface) i.next();
            clients.remove(client);
        }
    }
}
