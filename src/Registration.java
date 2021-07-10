import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;


public class Registration extends UnicastRemoteObject implements Registration_interface {

    private int porta_rmi = 4201;
    private CbServerImplementation server;

    public Registration(CbServerImplementation server) throws RemoteException {
        super();
        this.server = server;
    }

    public void start(){
        try {
            //Registration_interface stub = (Registration_interface) UnicastRemoteObject.exportObject(this, 5000);
            Registry registry = LocateRegistry.createRegistry(porta_rmi);
            registry.rebind("Sign in", this);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

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
