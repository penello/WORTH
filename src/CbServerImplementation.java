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

    /**
     * lista dei client registrati al servizio di notifica
     */
    private List<CbClientInterface> clients;

    /**
     * costruttore
     * @throws RemoteException se fallisce l'esportazione dell'oggetto
     */
    public CbServerImplementation() throws RemoteException{
        super();
        clients = new ArrayList<CbClientInterface>();
    }

    /**
     * metodo usato per serializzare la struttura dati prima di mandarla al client
     * @return un byte array contenente la struttura dati serializzata
     * @throws IOException
     */
    private byte[] serialization_map() throws IOException {
        ConcurrentHashMap<String, User> utenti = Singleton_db_utenti.getInstanceUtenti().get_concurrent_hashmap();
        ByteArrayOutputStream byteout = new ByteArrayOutputStream();
        ObjectOutputStream output = new ObjectOutputStream(byteout);
        output.writeObject(utenti);
        output.flush();
        output.close();
        byteout.close();
        byte b[] = byteout.toByteArray();
        return b;
    }

    /**
     * metodo usato per registrare un utente al servizio di notifica, gli viene passato anche la struttura dati serializzata contenente le informazioni necessarie all'utente
     * @param callbackClient
     * @throws IOException
     * @throws ClassNotFoundException Class of a serialized object cannot be found
     */
    public synchronized void RegisterForCallback(CbClientInterface callbackClient) throws IOException, ClassNotFoundException {
        if (!clients.contains(callbackClient)) {
            clients.add(callbackClient);
            byte b[] = serialization_map();
            callbackClient.notifyUtenti(b);
        }
    }

    /**
     * metodo usato per deregistrare un utente al servizio di notifica
     * @param callbackClient
     * @throws RemoteException
     */
    public synchronized void UnregisterForCallback(CbClientInterface callbackClient) throws RemoteException {
        clients.remove(callbackClient);
    }

    /**
     * metodo usato per notificare un cambiamento ai clients
     * @param utente
     * @throws RemoteException
     */
    public void notifica (User utente) throws RemoteException {
        doCallbacks(utente);
    }

    /**
     * metodo usato per notificare un cambiamento ai clients
     * @param utente
     * @throws RemoteException
     */
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
