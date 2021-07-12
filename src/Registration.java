import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;


public class Registration extends UnicastRemoteObject implements Registration_interface {

    private int porta_rmi;
    private CbServerImplementation server;

    public Registration(CbServerImplementation server, int port) throws RemoteException {
        super();
        porta_rmi = port;
        this.server = server;
    }

    public void start(){
        try {
            Registry registry = LocateRegistry.createRegistry(porta_rmi);
            registry.rebind("Sign in", this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * metodo usato per effettuare la registrazione di un client al servizio
     * @param nickutente
     * @param Password
     * @return il risultato dell'operazione
     * @throws RemoteException
     */
    public String register(String nickutente, String Password) throws RemoteException{
        Hash_users obj = Singleton_db_utenti.getInstanceUtenti();
        String ret;

        if(!nickutente.isEmpty() && !Password.isEmpty()){
            ret = obj.add_user(nickutente,Password);
            Persistent_data prs = Persistent_data.getInstance();
            if(!prs.save(prs.getUser_folder()+"utenti.json",obj, Hash_users.class)) return "impossibile salvare in maniera persistente l'utente";
            User utente = Singleton_db_utenti.getInstanceUtenti().get_user(nickutente);
            server.notifica(utente);
        }
        else return "nickutente e password non validi";
        return ret;
    }
}
